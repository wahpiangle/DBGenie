import { Role } from "@/types/role";
import { z } from "zod";

export const LoginSchema = z.object({
    email: z.string().email({
        message: "Please enter a valid email"
    }),
    password: z.string().min(1, {
        message: "Please enter a valid password",
    }),
})

export const RegisterSchema = z.object({
    name: z.string(),
    email: z.string().email({
        message: "Please enter a valid email"
    }),
    password: z.string().min(1, {
        message: "Please enter a valid password",
    }),
    role: z.nativeEnum(Role),
});

export const createPropertySchema = z.object({
    name: z.string().min(1),
    description: z.string().min(1),
    imageFiles: z.array(
        z.instanceof(File)
    ).min(1),
});