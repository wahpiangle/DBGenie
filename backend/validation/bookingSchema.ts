import { z } from "zod";

const CreateBookingSchema = z.object({
    propertyId: z.string(),
    checkIn: z.number(),
    checkOut: z.number(),
    tenantEmail: z.string(),
    rentalPrice: z.string(),
    remarks: z.string().nullable(),
    rentCollectionDay: z.number().min(1).max(28)
}).refine((data) => {
    return !isNaN(parseFloat(data.rentalPrice));
}, {
    message: 'Rental price must be a number'
});

const CheckBookingsByPropertySchema = z.object({
    propertyId: z.string(),
    checkIn: z.date(),
    checkOut: z.date()
});

export { CreateBookingSchema, CheckBookingsByPropertySchema };