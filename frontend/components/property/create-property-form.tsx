'use client';
import { createPropertySchema } from "@/schemas"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { Button } from "@/components/ui/button"
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { ArrowLeftCircle } from "lucide-react";
import axios from "axios";
import { toast } from "../ui/use-toast";
import { useRef, useState } from "react";
import { Spinner } from "../ui/spinner";

export default function CreatePropertyForm() {
    const [loading, setLoading] = useState(false)
    const fileInputRef = useRef<HTMLInputElement | null>(null);

    const form = useForm<z.infer<typeof createPropertySchema>>({
        resolver: zodResolver(createPropertySchema),
        defaultValues: {
            name: "",
            description: "",
            imageFiles: [],
        },
    })

    async function onSubmit(values: z.infer<typeof createPropertySchema>) {
        try {
            setLoading(true)
            const formData = new FormData();
            values.imageFiles.forEach((file) => {
                formData.append("files", file);
            });
            formData.append("name", values.name);
            formData.append("description", values.description);
            const res = await axios.post('http://localhost:8080/properties', formData, {
                withCredentials: true,
            })
            toast({
                title: "Success",
                description: "Property created successfully.",
            })
            form.reset()
            form.setValue('imageFiles', [])
            if (fileInputRef.current) {
                fileInputRef.current.value = "";
            }
        } catch (error) {
            toast({
                title: "Error",
                description: "Failed to create property.",
                variant: "destructive",
            })
        } finally {
            setLoading(false)
        }
    }
    return (
        <>
            <div className="flex items-center mb-8 gap-2">
                <ArrowLeftCircle className='cursor-pointer hover:scale-110' onClick={() => window.history.back()} />
                <h1 className='text-2xl font-semibold'>
                    Create Property
                </h1>
            </div>
            <Form {...form} >
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                    <FormField
                        control={form.control}
                        name="name"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Property Name</FormLabel>
                                <FormControl>
                                    <Input placeholder="Property Name" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="description"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Property Description</FormLabel>
                                <FormControl>
                                    <Input placeholder="Property Description" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="imageFiles"
                        render={({ field: { value, onChange, ...fieldProps } }) => (
                            <FormItem>
                                <FormLabel>Picture</FormLabel>
                                <FormControl>
                                    <Input
                                        {...fieldProps}
                                        placeholder="Picture"
                                        type="file"
                                        ref={fileInputRef}
                                        accept="image/*, application/pdf"
                                        multiple
                                        onChange={(event) =>
                                            onChange(
                                                Array.from(event.target.files || []).map(
                                                    (file) => file
                                                )
                                            )
                                        }
                                    />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <Button type="submit" disabled={loading}>
                        {loading ? <Spinner /> : "Create Property"}
                    </Button>
                    {/* <Button type="submit">Submit</Button> */}
                </form>
            </Form>
        </>
    )
}
