import { z } from 'zod';

const CreatePropertySchema = z.object({
    name: z.string().min(1).max(255),
    description: z.string().min(1).max(255),
    rentalPerMonth: z.string().refine((val) => {
        return !isNaN(parseFloat(val));
    }),
});

const updatePropertySchema = z.object({
    name: z.string().min(3).max(255),
    description: z.string().min(3).max(255),
    rentalPerMonth: z.string().refine((val) => {
        return !isNaN(parseFloat(val));
    }),
    updateImage: z.boolean(),
});

export { CreatePropertySchema, updatePropertySchema };