'use client';
import { createPropertySchema } from "@/schemas"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"
import axios from "axios";
import { toast } from "../ui/use-toast";
import API_URL from "@/constants";
import PropertyForm from "./property-form";
import { useRef, useState } from "react";
import { ArrowLeftCircle } from "lucide-react";
import { useRouter } from "next/navigation";

export default function CreatePropertyForm() {
    const [loading, setLoading] = useState(false);
    const fileInputRef = useRef<HTMLInputElement | null>(null);
    const router = useRouter();
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
            const res = await axios.post(`${API_URL}/properties`,
                formData,
                {
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
            router.push('/properties')
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
            <PropertyForm
                form={form}
                onSubmit={onSubmit}
                fileInputRef={fileInputRef}
                loading={loading}
            />
        </>

    )
}
