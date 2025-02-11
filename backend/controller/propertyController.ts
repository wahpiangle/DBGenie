import type { Request, Response } from "express";
import { prisma } from "../prisma";
import { CreatePropertySchema, updatePropertySchema } from "../validation/propertySchemas";
import { ZodError } from "zod";
import { Role } from "@prisma/client";
import { firebaseApp } from "../lib/firebase";
import { getStorage } from "firebase-admin/storage";

export class PropertyController {
    public static async createProperty(req: Request, res: Response) {
        const { user } = req.session;
        const storage = getStorage(firebaseApp).bucket("gs://fittrack-61776.appspot.com")
        const files = req.files as Express.Multer.File[];
        try {
            // remove the quotation marks from the body
            CreatePropertySchema.parse({
                name: req.body.name,
                description: req.body.description,
            });
            const property = await prisma.property.create({
                data: {
                    name: req.body.name,
                    description: req.body.description,
                    created_by: {
                        connect: {
                            id: user.id
                        }
                    }
                },
            });
            const fileUrls = await Promise.all(
                files.map(async (file: Express.Multer.File) => {
                    const uploadResponse = await storage.upload(file.path, {
                        destination: `properties/${property.id}/${file.originalname}`,
                    });

                    const fileUrl = await uploadResponse[0].getSignedUrl({
                        expires: '03-09-2491',
                        action: 'read',
                    });

                    return fileUrl[0];
                })
            );
            await prisma.property.update({
                where: {
                    id: property.id
                },
                data: {
                    image_url: fileUrls
                }
            });

            res.json(property);
            return;
        } catch (error) {
            console.log(error);
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
            const { name, description } = req.body;
            updatePropertySchema.parse(
                {
                    name,
                    description,
                }
            );
            const { id } = req.params;
            let data: {
                name?: string,
                description?: string,
                image_url?: string[]
            } = { name, description, image_url: [] };
            const storage = getStorage(firebaseApp).bucket("gs://fittrack-61776.appspot.com")
            const imageFiles = req.files as Express.Multer.File[];
            if (imageFiles) {
                await storage.deleteFiles({
                    prefix: `properties/${id}`
                });
                console.log("imageFiles", imageFiles)
                const fileUrls = await Promise.all(
                    imageFiles.map(async (file: Express.Multer.File) => {
                        const [uploadResponse] = await storage.upload(file.path, {
                            destination: `properties/${id}/${file.originalname}`,
                        });

                        const [fileUrl] = await uploadResponse.getSignedUrl({
                            expires: '03-09-2491',
                            action: 'read',
                        });

                        return fileUrl;
                    })
                );

                data.image_url = fileUrls;
            }
            const property = await prisma.property.update({
                where: { id },
                data,
            });
            res.json(property);
            return;
        } catch (error) {
            console.log(error);
            if (error instanceof ZodError) {
                res.status(400).json({ error: error.errors });
            } else {
                res.status(500).json({ error: 'Internal server error' });
            }
            return;
        }
    }

    public static async getPropertiesByUser(req: Request, res: Response) {
        const { user } = req.session;
        const properties =
            user.role === Role.MANAGER ?
                await prisma.property.findMany({
                    where: {
                        user_id: user.id
                    },
                    include: {
                        bookings: true
                    }
                })
                :
                await prisma.property.findMany({
                    where: {
                        bookings: {
                            some: {
                                user_id: user.id
                            }
                        }
                    },
                    include: {
                        bookings: true
                    }
                })
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
                bookings: {
                    include: {
                        user: true
                    }
                },
            },
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
        const storage = getStorage(firebaseApp).bucket("gs://fittrack-61776.appspot.com")
        await storage.deleteFiles({
            prefix: `properties/${id}`
        });
        res.json(property);
        return;
    }
}