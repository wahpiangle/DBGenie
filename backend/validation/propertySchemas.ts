import { z } from 'zod';

const CreatePropertySchema = z.object({
    name: z.string().min(1).max(255),
    description: z.string().min(1).max(255),
});

const updatePropertySchema = z.object({
    name: z.string().min(3).max(255),
    description: z.string().min(3).max(255),
    updateImage: z.string(),
});

export { CreatePropertySchema, updatePropertySchema };