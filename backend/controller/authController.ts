import { Role } from "@prisma/client";
import { prisma } from "../prisma";
import bcrypt from "bcrypt";
import type { Request, Response } from "express";
export class AuthController {
    public static async register(req: Request, res: Response) {
        const { name, email, password } = req.body;
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
                role: Role.TENANT
            }
        });

        req.session.user = user;
        res.json(user);
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
        return;
    }

    public static logout(req: Request, res: Response) {
        req.session.destroy(err => {
            if (err) {
                res.redirect('/');
                return;
            }
            res.clearCookie('sid');
            res.redirect('/auth/login');
            return;
        });
    }
}