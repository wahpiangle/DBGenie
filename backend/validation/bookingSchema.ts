import { z } from "zod";

const CreateBookingSchema = z.object({
    propertyId: z.string(),
    checkIn: z.string(),
    checkOut: z.string(),
    tenantEmail: z.string(),
    rentalPrice: z.number(),
    remarks: z.string().nullable(),
    rentCollectionDay: z.number().min(1).max(28)
});

const CheckBookingsByPropertySchema = z.object({
    propertyId: z.string(),
    checkIn: z.date(),
    checkOut: z.date()
});

export { CreateBookingSchema, CheckBookingsByPropertySchema };