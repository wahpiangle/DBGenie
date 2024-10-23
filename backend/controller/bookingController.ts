import type { Request, Response } from "express";
import { CheckBookingsByPropertySchema, CreateBookingSchema } from "../validation/bookingSchema";
import { prisma } from "../prisma";
import { ZodError } from "zod";

export class BookingController {
    public static async createBooking(req: Request, res: Response) {
        const { propertyId, checkIn, checkOut, rentalPrice, remarks, rentCollectionDay } = req.body;
        const user = req.session.user;
        try {
            CreateBookingSchema.parse({
                propertyId,
                checkIn,
                checkOut,
                rentalPrice,
                rentCollectionDay,
                remarks
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
            if (error instanceof ZodError) {
                res.status(400).json({ error: error.errors });
            }
            else {
                res.status(500).json({ error: 'Internal server error' });
            }
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