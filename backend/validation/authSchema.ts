import { Role } from "@prisma/client";
import { z } from "zod";

const RegisterSchema = z.object({
    name: z.string(),
    email: z.string().email(),
    password: z.string().min(6),
    role: z.nativeEnum(Role)
});

export { RegisterSchema };