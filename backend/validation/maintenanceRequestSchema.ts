import { z } from "zod";

const CreateMaintenanceRequestSchema = z.object({
    propertyId: z.string(),
    description: z.string(),
});

export { CreateMaintenanceRequestSchema };