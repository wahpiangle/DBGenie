import { z } from "zod";

const CreateBookingSchema = z.object({
    propertyId: z.string(),
    checkIn: z.string(),
    checkOut: z.string(),
    userId: z.string()
});

export { CreateBookingSchema };