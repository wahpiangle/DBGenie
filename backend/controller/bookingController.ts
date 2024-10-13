import type { Request, Response } from "express";
import { CreateBookingSchema } from "../validation/bookingSchema";
import { prisma } from "../prisma";
import { ZodError } from "zod";

export class BookingController {
    public static async createBooking(req: Request, res: Response) {
        const { propertyId, checkIn, checkOut } = req.body;
        const user = req.session.user;
        try {
            CreateBookingSchema.parse({
                propertyId,
                checkIn,
                checkOut
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
                res.status(400).json({ error: 'Property is not available' });
                return;
            }
            const booking = await prisma.booking.create({
                data: {
                    propertyId,
                    checkIn,
                    checkOut,
                    userId: user.id
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
}