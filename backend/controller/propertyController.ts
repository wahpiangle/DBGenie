import type { Request, Response } from "express";
import { prisma } from "../prisma";
import { CreatePropertySchema, updatePropertySchema } from "../validation/propertySchemas";
import { ZodError } from "zod";

export class PropertyController {
    public static async createProperty(req: Request, res: Response) {
        const { user } = req.session;
        try {
            CreatePropertySchema.parse({
                name: req.body.name,
                description: req.body.description,
            });
            const property = await prisma.property.create({
                data: {
                    name: req.body.name,
                    description: req.body.description,
                    price: req.body.price,
                    createdBy: {
                        connect: {
                            id: user.id
                        }
                    }
                },
            });
            res.json(property);
            return;
        } catch (error) {
            if (error instanceof ZodError) {
                res.status(400).json({ error: error.errors });
            } else {
                res.status(500).json({ error: 'Internal server error' });
            }
            return;
        }
    }

    public static async updateProperty(req: Request, res: Response) {
        try {
            updatePropertySchema.parse(req.body);
            const { id } = req.params;
            const property = await prisma.property.update({
                where: {
                    id
                },
                data: {
                    ...req.body
                }
            });
            res.json(property);
            return;
        } catch (error) {
            if (error instanceof ZodError) {
                res.status(400).json({ error: error.errors });
            } else {
                res.status(500).json({ error: 'Internal server error' });
            }
            return;
        }
    }

    public static async getProperties(req: Request, res: Response) {
        const properties = await prisma.property.findMany();
        res.json(properties);
        return;
    }

    public static async getProperty(req: Request, res: Response) {
        const { id } = req.params;
        const property = await prisma.property.findUnique({
            where: {
                id
            },
            include: {
                MaintenanceRequest: true,
                bookings: true,
            }
        });
        res.json(property);
        return;
    }

    public static async removeProperty(req: Request, res: Response) {
        const { id } = req.params;
        const property = await prisma.property.delete({
            where: {
                id
            }
        });
        res.json(property);
        return;
    }
}