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

export const editPropertySchema = z.object({
    name: z.string(),
    description: z.string(),
    imageFiles: z.array(
        z.instanceof(File)
    ),
});

export const createBookingSchema = z.object({
    date: z.object({
        from: z.date(),
        to: z.date(),
    }),
    tenantEmail: z.string().email(),
    rentalPrice: z.coerce.number().gte(1),
    remarks: z.string(),
    rentCollectionDay: z.coerce.number().gte(1).lte(28),
});