import type { Request, Response } from "express";
import { prisma } from "../prisma";

export class PaymentController {
    public static async createPayment(req: Request, res: Response) {
        const { user } = req.session;
        const { amount, bookingId } = req.body;
        try {
            const booking = await prisma.booking.findUnique({
                where: {
                    id: bookingId
                }
            });
            if (!booking) {
                res.status(400).json({ error: 'Booking not found' });
                return;
            }

            const payment = await prisma.payment.create({
                data: {
                    amount,
                    bookingId,
                    userId: user.id
                }
            });
            res.json(payment);
            return;
        } catch (error) {
            res.status(500).json({ error: 'Internal server error' });
            return;
        }
    }
}