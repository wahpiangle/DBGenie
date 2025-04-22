'use client'
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
import { useContext, useTransition } from "react";
import { RegisterSchema } from "@/schemas";
import { AuthContext } from "@/context/AuthContext";
import { useRouter } from "next/navigation";
import { CardWrapper } from "@/components/auth/card-wrapper";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Role } from "@/types/role";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";

export default function RegisterForm() {
    const router = useRouter();
    const [isPending, startTransition] = useTransition();
    const { register } = useContext(AuthContext);
    const form = useForm<z.infer<typeof RegisterSchema>>({
        resolver: zodResolver(RegisterSchema),
        defaultValues: {
            name: "",
            email: "",
            password: "",
            role: Role.USER,
        },
    })

    function onSubmit(values: z.infer<typeof RegisterSchema>) {
        startTransition(() => register(values.email, values.password, values.name, values.role)
            .then(() => {
                router.push("/");
            }
            ));
    }
    return (
        <CardWrapper
            headerLabel="Create an Account"
            backButtonLabel="Already have an account?"
            backButtonHref="/login"
        >
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                    <div className="space-y-4">
                        <FormField
                            control={form.control}
                            name="name"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Name</FormLabel>
                                    <FormControl>
                                        <Input placeholder="John Doe" {...field} disabled={isPending} type="text" />
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
                                        <Input placeholder="john@example.com"
                                            {...field} disabled={isPending} type="email" />
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
                        <FormField
                            control={form.control}
                            name="role"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Role</FormLabel>
                                    <FormControl>
                                        <Tabs
                                            defaultValue={Object.values(Role)[0]}
                                            onValueChange={(value) => form.setValue("role", value as Role)}
                                        >
                                            <TabsList className="grid w-full grid-cols-2">
                                                {Object.values(Role).map((role) => (
                                                    <TabsTrigger key={role} value={role}>
                                                        {role}
                                                    </TabsTrigger>
                                                ))}
                                            </TabsList>
                                        </Tabs>
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                    </div>
                    <Button type="submit" disabled={isPending} className="w-full">
                        Register
                    </Button>
                </form>
            </Form>
        </CardWrapper >
    )
}