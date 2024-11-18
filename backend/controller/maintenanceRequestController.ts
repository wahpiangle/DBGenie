import type { Request, Response } from "express";
import { CreateMaintenanceRequestSchema } from "../validation/maintenanceRequestSchema";
import { prisma } from "../prisma";
import { ZodError } from "zod";
import { getStorage } from "firebase-admin/storage";
import { firebaseApp } from "../lib/firebase";
import { Role } from "@prisma/client";

export class MaintenanceRequestController {
    public static async createMaintenanceRequest(req: Request, res: Response) {
        const { propertyId, description } = req.body;
        const storage = getStorage(firebaseApp).bucket("gs://fittrack-61776.appspot.com")
        const files = req.files as Express.Multer.File[];
        const user = req.session.user;
        try {
            CreateMaintenanceRequestSchema.parse({
                propertyId,
                description
            });
            const property = await prisma.property.findUnique({
                where: {
                    id: propertyId
                }
            });
            if (!property) {
                res.status(400).json({ error: 'Property not found' });
                return;
            }

            // check if user is tenant of property
            const booking = await prisma.booking.findFirst({
                where: {
                    propertyId,
                    userId: user.id,
                    checkIn: {
                        lte: new Date()
                    },
                    checkOut: {
                        gte: new Date()
                    }
                }
            });
            if (!booking) {
                res.status(400).json({ error: 'User is not tenant of property' });
                return;
            }
            let maintenanceRequest = await prisma.maintenanceRequest.create({
                data: {
                    propertyId,
                    description,
                    userId: user.id
                },
            });
            const fileUrls = await Promise.all(
                files.map(async (file: Express.Multer.File) => {
                    const uploadResponse = await storage.upload(file.path, {
                        destination: `maintenanceRequests/${maintenanceRequest.id}/${file.originalname}`,
                    });

                    const fileUrl = await uploadResponse[0].getSignedUrl({
                        expires: '03-09-2491',
                        action: 'read',
                    });

                    return fileUrl[0];
                })
            );

            maintenanceRequest = await prisma.maintenanceRequest.update({
                where: {
                    id: maintenanceRequest.id
                },
                data: {
                    imageUrl: fileUrls
                }
            });
            res.json(maintenanceRequest);
            return;
        } catch (error) {
            if (error instanceof ZodError) {
                res.status(400).json({ error: error.errors });
            }
            else {
                res.status(500).json({ error: 'Internal server error' });
            }
            return;
        }
    }

    public static async getMaintenanceRequestByProperty(req: Request, res: Response) {
        const { propertyId } = req.params;
        try {
            const maintenanceRequests = await prisma.maintenanceRequest.findMany({
                where: {
                    propertyId
                }
            });
            res.json(maintenanceRequests);
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

    public static async getMaintenanceRequestByUser(req: Request, res: Response) {
        const user = req.session.user;
        try {
            if (user.role === Role.MANAGER) {
                const maintenanceRequests = await prisma.maintenanceRequest.findMany({
                    where: {
                        property: {
                            userId: user.id
                        }
                    }
                });

                res.json(maintenanceRequests);
                return;
            } else if (user.role === Role.TENANT) {
                const maintenanceRequests = await prisma.maintenanceRequest.findMany({
                    where: {
                        userId: user.id
                    }
                });
                res.json(maintenanceRequests);
                return;
            }
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

    public static async createMaintenanceRequestUpdate(req: Request, res: Response) {
        const { maintenanceRequestId, description } = req.body;
        const user = req.session.user;

        try {
            const maintenanceRequest = await prisma.maintenanceRequest.findUnique({
                where: {
                    id: maintenanceRequestId
                }
            });

            if (!maintenanceRequest) {
                res.status(400).json({ error: 'Maintenance request not found' });
                return;
            }

            const maintenanceRequestUpdate = await prisma.maintenanceRequestUpdate.create({
                data: {
                    maintenanceRequestId,
                    description,
                    userId: user.id
                }
            });

            res.json(maintenanceRequestUpdate);
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
}