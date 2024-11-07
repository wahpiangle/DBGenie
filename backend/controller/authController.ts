import { Role } from "@prisma/client";
import { prisma } from "../prisma";
import bcrypt from "bcrypt";
import type { Request, Response } from "express";
import { ZodError } from "zod";
import { RegisterSchema } from "../validation/authSchema";
import { sendEmail } from "../helper/email";
export class AuthController {
    public static async register(req: Request, res: Response) {
        const { name, email, password, role } = req.body;
        try {
            RegisterSchema.parse(req.body);
            const existingUser = await prisma.user.findUnique({
                where: {
                    email
                }
            });
            if (existingUser) {
                res.status(400).json({ error: 'User already exists' });
                return;
            }
            const hashedPassword = await bcrypt.hash(password, 12);
            const user = await prisma.user.create({
                data: {
                    name,
                    email,
                    password: hashedPassword,
                    role: role as Role
                }
            });

            const token = await prisma.verificationToken.create({
                data: {
                    userId: user.id,
                    token: Math.floor(100000 + Math.random() * 900000).toString()
                }
            });
            await sendEmail(user.email, 'Verify your account', `Your verification code is ${token.token}`);

            req.session.user = user;
            res.json(user);
            return;
        } catch (error) {
            if (error instanceof ZodError) {
                res.status(400).json({ error: error.errors[0].message });
            }
            else {
                res.status(500).json({ error: 'Internal server error' });
            }
            return;
        }
    }

    public static async verifyAccount(req: Request, res: Response) {
        const { user } = req.session;
        const { code } = req.body;
        const userWithCode = await prisma.user.findUnique({
            where: {
                id: user.id
            },
            include: {
                verificationToken: true
            }
        });

        if (userWithCode?.verified) {
            res.status(400).json({ error: 'User already verified' });
            return;
        }

        if (!userWithCode?.verificationToken) {
            res.status(400).json({ error: 'No verification token found' });
            const token = await prisma.verificationToken.create({
                data: {
                    userId: user.id,
                    token: Math.floor(100000 + Math.random() * 900000).toString(),
                    createdAt: new Date()
                }
            });
            await sendEmail(user.email, 'Verify your account', `Your verification code is ${token.token}`);
            return;
        }

        // 1 hour expiry
        if (userWithCode?.verificationToken?.createdAt < new Date(new Date().getTime() - 60 * 60 * 1000)) {
            res.status(400).json({ error: 'Token expired, a new one has been sent' });
            await prisma.verificationToken.update({
                where: {
                    id: userWithCode.verificationToken.id
                },
                data: {
                    token: Math.floor(100000 + Math.random() * 900000).toString(),
                    createdAt: new Date()
                }
            });
            return;
        }

        if (userWithCode?.verificationToken?.token !== code) {
            res.status(400).json({ error: 'Invalid code' });
            return;
        }
        await prisma.user.update({
            where: {
                id: user.id
            },
            data: {
                verified: true
            }
        });
        req.session.user = { ...user, verified: true };
        res.json({ message: 'Email verified' });
        return;
    }

    public static async login(req: Request, res: Response) {
        const { email, password } = req.body;
        const user = await prisma.user.findUnique({
            where: {
                email
            }
        });
        if (!user) {
            res.status(400).json({ error: 'Invalid credentials' });
            return;
        }
        const isPasswordValid = await bcrypt.compare(password, user.password);
        if (!isPasswordValid) {
            res.status(400).json({ error: 'Invalid credentials' });
            return;
        }
        req.session.user = user;
        res.json(user);
        return;
    }

    public static logout(req: Request, res: Response) {
        req.session.destroy(err => {
            if (err) {
                res.redirect('/');
                return;
            }
            res.clearCookie('sid');
            res.json({ message: 'Logged out' });
            return;
        });
    }
}