import { z } from "zod";

const CreateBookingSchema = z.object({
    propertyId: z.string(),
    checkIn: z.string(),
    checkOut: z.string(),
    userId: z.string()
});

const CheckBookingsByPropertySchema = z.object({
    propertyId: z.string(),
    checkIn: z.date(),
    checkOut: z.date()
});

export { CreateBookingSchema, CheckBookingsByPropertySchema };