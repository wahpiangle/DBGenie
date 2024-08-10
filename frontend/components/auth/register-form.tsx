
'use client';
import Link from 'next/link'
import { Loader2 } from "lucide-react"
import { useForm } from 'react-hook-form'
import { Button } from '../ui/button'
import { FormField, FormItem, FormLabel, FormControl, FormMessage, Form } from '../ui/form'
import { CardWrapper } from './card-wrapper'
import { Input } from '../ui/input'
import { registerSchema } from '@/schemas/AuthSchema'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { FormError } from './form-error';

export const RegisterForm = () => {
    const router = useRouter();
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>("");
    const form = useForm<z.infer<typeof registerSchema>>({
        resolver: zodResolver(registerSchema),
        defaultValues: {
            name: "",
            email: "",
            password: ""
        }
    })

    async function onSubmit(values: z.infer<typeof registerSchema>) {
        try {
            setError("");
            setLoading(true);
            const validatedFields = registerSchema.safeParse(values);

            if (!validatedFields.success) {
                throw (Error("Invalid fields"));
            }
            // REGISTER
        } catch (e) {
            setError((e as Error).message);
        } finally {
            setLoading(false);
        }

    }
    return (
        <CardWrapper
            headerLabel="Register"
            backButtonHref="/login"
            backButtonLabel="Already have an account?"
        >
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-2">
                    <FormField
                        control={form.control}
                        name="name"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Name</FormLabel>
                                <FormControl>
                                    <Input placeholder="John Doe" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="email"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Email</FormLabel>
                                <FormControl>
                                    <Input placeholder="test@example.com" {...field} />
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
                                    <Input placeholder="********" type="password" {...field} />
                                </FormControl>
                                <FormMessage />
                                <Button
                                    size="sm"
                                    variant="link"
                                    asChild
                                    className="px-0 font-normal"
                                >
                                    <Link href="/reset-password">
                                        Forgot Password?
                                    </Link>
                                </Button>
                            </FormItem>
                        )}
                    />
                    <FormError message={error} />
                    <Button type="submit" className="w-full bg-primary hover:bg-primary-bright" disabled={loading}>
                        {
                            loading &&
                            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                        }
                        {loading ? "Loading..." : "Login"}
                    </Button>
                </form>
            </Form>
        </CardWrapper>
    )
}
