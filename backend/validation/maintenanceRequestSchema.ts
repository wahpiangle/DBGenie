import { z } from "zod";

const CreateMaintenanceRequestSchema = z.object({
    propertyId: z.string(),
    title: z.string(),
    description: z.string().optional()
});

export { CreateMaintenanceRequestSchema };