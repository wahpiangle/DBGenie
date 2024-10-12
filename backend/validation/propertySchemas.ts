import { z } from 'zod';

const CreatePropertySchema = z.object({
    name: z.string().min(3).max(255),
    description: z.string().min(3).max(255),
    price: z.number().min(0),
});

const updatePropertySchema = z.object({
    name: z.string().min(3).max(255).optional(),
    description: z.string().min(3).max(255).optional(),
    price: z.number().min(0).optional(),
});

export { CreatePropertySchema, updatePropertySchema };