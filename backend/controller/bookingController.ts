import type { Request, Response } from "express";
import { CheckBookingsByPropertySchema, CreateBookingSchema } from "../validation/bookingSchema";
import { prisma } from "../prisma";
import { ZodError } from "zod";

export class BookingController {
    public static async createBooking(req: Request, res: Response) {
        let { propertyId, checkIn, checkOut, tenantEmail, rentalPrice, remarks, rentCollectionDay } = req.body;
        const user = req.session.user;
        try {
            CreateBookingSchema.parse({
                propertyId,
                checkIn,
                checkOut,
                tenantEmail,
                rentalPrice,
                rentCollectionDay,
                remarks
            });

            if (checkIn >= checkOut) {
                res.status(400).json({ error: 'Check-out date must be greater than check-in date' });
            }

            checkIn = new Date(checkIn);
            checkOut = new Date(checkOut);

            const user = await prisma.user.findFirst({
                where: {
                    email: tenantEmail
                }
            });
            if (!user) {
                res.status(400).json({ error: 'Tenant not found' });
                return;
            }

            const property = await prisma.property.findUnique({
                where: {
                    id: propertyId
                }
            });
            if (!property) {
                res.status(400).json({ error: 'Property not found' });
                return;
            }
            // convert checkOut and checkIn from milliseconds to Date
            const bookings = await prisma.booking.findMany({
                where: {
                    propertyId: propertyId,
                    checkIn: {
                        lte: checkOut
                    },
                    checkOut: {
                        gte: checkIn
                    }
                }
            });

            if (bookings.length > 0) {
                res.status(400).json({ error: 'Property is already booked for the selected dates' });
                return;
            }

            const booking = await prisma.booking.create({
                data: {
                    propertyId,
                    checkIn,
                    checkOut,
                    userId: user.id,
                    rentalPrice,
                    rentCollectionDay,
                    remarks
                },
            });
            res.json(booking);
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

    public static async getBooking(req: Request, res: Response) {
        const bookingId = req.params.id;
        const user = req.session.user;
        try {
            const booking = await prisma.booking.findUnique({
                where: {
                    id: bookingId
                },
                include: {
                    property: {
                        include: {
                            createdBy: {
                                select: {
                                    id: true,
                                }
                            }
                        }
                    },
                }
            });
            if (!booking) {
                res.status(400).json({ error: 'Booking not found' });
                return;
            }
            if (booking.userId !== user.id && booking.property.createdBy.id !== user.id) {
                res.status(403).json({ error: 'You are not authorized to view this booking' });
                return;
            }
            res.json(booking);
            return;
        } catch (error) {
            console.log(error);
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
    }

    public static async checkBookingsByProperty(req: Request, res: Response) {
        const propertyId = req.params.propertyId;
        const { checkIn, checkOut } = req.body;
        try {
            CheckBookingsByPropertySchema.parse({
                propertyId,
                checkIn,
                checkOut,
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
            // check if the property is available
            const bookings = await prisma.booking.findMany({
                where: {
                    propertyId: propertyId,
                    checkIn: {
                        lte: checkOut
                    },
                    checkOut: {
                        gte: checkIn
                    }
                }
            });
            if (bookings.length > 0) {
                res.json({ available: false });
                return;
            }
            res.json({ available: true });
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

    public static async editBooking(req: Request, res: Response) {
        const bookingId = req.params.id;
        const { checkIn, checkOut } = req.body;
        const user = req.session.user;
        try {
            const booking = await prisma.booking.findUnique({
                where: {
                    id: bookingId
                },
                include: {
                    property: {
                        include: {
                            createdBy: true
                        }
                    },
                }
            });
            if (!booking) {
                res.status(400).json({ error: 'Booking not found' });
                return;
            }
            if (booking.userId !== user.id || booking.property.createdBy.id !== user.id) {
                res.status(403).json({ error: 'You are not authorized to edit this booking' });
                return;
            }
            await prisma.booking.update({
                where: {
                    id: bookingId
                },
                data: {
                    checkIn,
                    checkOut
                }
            });
            res.json({ message: 'Booking updated successfully' });
            return;
        } catch (error) {
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
    }

    public static async deleteBooking(req: Request, res: Response) {
        const bookingId = req.params.id;
        const user = req.session.user;
        try {
            const booking = await prisma.booking.findUnique({
                where: {
                    id: bookingId
                },
                include: {
                    property: {
                        include: {
                            createdBy: true
                        }
                    },
                }
            });
            if (!booking) {
                res.status(400).json({ error: 'Booking not found' });
                return;
            }
            if (booking.userId !== user.id || booking.property.createdBy.id !== user.id) {
                res.status(403).json({ error: 'You are not authorized to delete this booking' });
                return;
            }
            await prisma.booking.delete({
                where: {
                    id: bookingId
                }
            });
            res.json({ message: 'Booking deleted successfully' });
            return;
        } catch (error) {
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
    }
}