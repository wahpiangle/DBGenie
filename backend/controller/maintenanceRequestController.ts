import type { Request, Response } from "express";
import { CreateMaintenanceRequestSchema } from "../validation/maintenanceRequestSchema";
import { prisma } from "../prisma";
import { ZodError } from "zod";
import { getStorage } from "firebase-admin/storage";
import { firebaseApp } from "../lib/firebase";
import { Role } from "@prisma/client";

export class MaintenanceRequestController {
    public static async createMaintenanceRequest(req: Request, res: Response) {
        const { propertyId, title, description } = req.body;
        const storage = getStorage(firebaseApp).bucket("gs://fittrack-61776.appspot.com")
        const files = req.files as Express.Multer.File[];
        const user = req.session.user;
        try {
            CreateMaintenanceRequestSchema.parse({
                propertyId,
                title,
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
                    property_id: propertyId,
                    user_id: user.id,
                    check_in: {
                        lte: new Date()
                    },
                    check_out: {
                        gte: new Date()
                    }
                }
            });
            if (!booking) {
                res.status(400).json({ error: 'User is not tenant of property' });
                return;
            }

            let maintenanceRequest = await prisma.maintenance_request.create({
                data: {
                    title,
                    property_id: propertyId,
                    description,
                    user_id: user.id
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

            maintenanceRequest = await prisma.maintenance_request.update({
                where: {
                    id: maintenanceRequest.id
                },
                data: {
                    image_url: fileUrls
                }
            });
            res.json(maintenanceRequest);
            return;
        } catch (error) {
            console.log(error);
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
            const maintenanceRequests = await prisma.maintenance_request.findMany({
                where: {
                    property_id: propertyId
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
            const maintenanceRequests = await prisma.maintenance_request.findMany({
                where: user.role === Role.MANAGER
                    ? { property: { user_id: user.id } }
                    : { user_id: user.id },
                include: {
                    property: true
                }
            });
            res.json(maintenanceRequests);
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
        const { description } = req.body;
        const { maintenanceRequestId } = req.params;
        const user = req.session.user;
        const storage = getStorage(firebaseApp).bucket("gs://fittrack-61776.appspot.com")
        const file = req.file as Express.Multer.File;
        try {
            const maintenanceRequest = await prisma.maintenance_request.findUnique({
                where: {
                    id: maintenanceRequestId
                },
                include: {
                    property: true
                }
            });

            if (!maintenanceRequest) {
                res.status(400).json({ error: 'Maintenance request not found' });
                return;
            }

            if (
                (user.role === Role.USER && maintenanceRequest.user_id !== user.id) ||
                (user.role === Role.MANAGER && maintenanceRequest.property.user_id !== user.id)
            ) {
                res.status(403).json({ error: 'You are not authorized to update this maintenance request' });
                return;
            }
            const maintenanceRequestUpdate = await prisma.maintenance_request_update.create({
                data: {
                    maintenanceRequestId,
                    description,
                    user_id: user.id,
                    image_url: ""
                }
            });
            if (file) {
                const [uploadResponse] = await storage.upload(file.path, {
                    destination: `maintenanceRequests/${maintenanceRequest.id}/${file.originalname}`,
                })

                const [fileUrl] = await uploadResponse.getSignedUrl({
                    expires: '03-09-2491',
                    action: 'read',
                });

                await prisma.maintenance_request_update.update({
                    where: {
                        id: maintenanceRequestUpdate.id
                    },
                    data: {
                        image_url: fileUrl
                    }
                });
            }

            res.json({ message: 'Maintenance request update created' });
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

    public static async getMaintenanceRequest(req: Request, res: Response) {
        const { id } = req.params;
        try {
            const maintenanceRequest = await prisma.maintenance_request.findUnique({
                where: {
                    id,
                },
                include: {
                    maintenance_request_update: {
                        orderBy: {
                            created_at: 'asc'
                        }
                    }
                },
            });
            res.json(maintenanceRequest);
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

    public static async resolveMaintenanceRequest(req: Request, res: Response) {
        const { id } = req.params;
        const user = req.session.user;
        try {
            const maintenanceRequest = await prisma.maintenance_request.findUnique({
                where: {
                    id
                },
                include: {
                    property: true
                }
            });
            if (!maintenanceRequest) {
                res.status(400).json({ error: 'Maintenance request not found' });
                return;
            }

            if (
                (user.role === Role.USER && maintenanceRequest.user_id !== user.id) ||
                (user.role === Role.MANAGER && maintenanceRequest.property.user_id !== user.id)
            ) {
                res.status(403).json({ error: 'You are not authorized to resolve this maintenance request' });
                return;
            }

            const resolvedMaintenanceRequest = await prisma.maintenance_request.update({
                where: {
                    id
                },
                data: {
                    resolved: true
                }
            });
            res.json(resolvedMaintenanceRequest);
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