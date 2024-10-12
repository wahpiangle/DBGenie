import { z } from "zod";

const RegisterSchema = z.object({
    email: z.string().email(),
    password: z.string().min(6),
});

export { RegisterSchema };