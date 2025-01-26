"use client";

import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { useContext, useTransition } from "react";
import { LoginSchema } from "@/schemas";
import { CardWrapper } from "./card-wrapper";
import { AuthContext } from "@/context/AuthContext";
import { useRouter } from "next/navigation";

export const LoginForm = () => {
    const router = useRouter();
    const [isPending, startTransition] = useTransition();
    const { login } = useContext(AuthContext);
    const form = useForm<z.infer<typeof LoginSchema>>({
        resolver: zodResolver(LoginSchema),
        defaultValues: {
            email: "",
            password: "",
        },
    })

    function onSubmit(values: z.infer<typeof LoginSchema>) {
        startTransition(() => login(values.email, values.password)
            .then(() => {
                router.push("/");
            }
            ));
    }

    return (
        <CardWrapper
            headerLabel="Welcome Back"
            backButtonLabel="Create an account"
            backButtonHref="/register"
        >
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                    <div className="space-y-4">
                        <FormField
                            control={form.control}
                            name="email"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Email</FormLabel>
                                    <FormControl>
                                        <Input placeholder="john@example.com" {...field} disabled={isPending} type="email" />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="password"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Password</FormLabel>
                                    <FormControl>
                                        <Input placeholder="******" {...field} disabled={isPending} type="password" />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button type="submit" disabled={isPending} className="w-full">
                        Login
                    </Button>
                </form>
            </Form>
        </CardWrapper>
    )
}