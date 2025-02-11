'use client';
import API_URL from '@/constants';
import { editPropertySchema } from '@/schemas';
import { zodResolver } from '@hookform/resolvers/zod';
import axios from 'axios';
import { useRouter } from 'next/navigation';
import React, { useEffect, useRef, useState } from 'react'
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { toast } from '../ui/use-toast';
import { ArrowLeftCircle } from 'lucide-react';
import PropertyForm from './property-form';
import { useQuery } from '@tanstack/react-query';
import { usePathname } from 'next/navigation';
import { Spinner } from '../ui/spinner';

export default function EditPropertyForm() {
    const propertyId = usePathname().split('/')[2];
    const [loading, setLoading] = useState(false);
    const { data, isLoading } = useQuery({
        queryKey: [`edit-property/${propertyId}`],
        queryFn: async () => {
            const response = await axios.get(`${API_URL}/properties/${propertyId}`, {
                withCredentials: true
            });
            return response.data
        },

    })
    const fileInputRef = useRef<HTMLInputElement | null>(null);
    const router = useRouter();
    const form = useForm<z.infer<typeof editPropertySchema>>({
        resolver: zodResolver(editPropertySchema),
        defaultValues: {
            name: "",
            description: "",
            imageFiles: [],
        },
    })

    const { reset } = form;

    useEffect(() => {
        if (data) {
            reset({
                name: data.name || "",
                description: data.description || "",
                imageFiles: [],
            });
        }
    }, [data, reset]);

    async function onSubmit(values: z.infer<typeof editPropertySchema>) {
        try {
            setLoading(true)
            const formData = new FormData();
            values.imageFiles.forEach((file) => {
                formData.append("files", file);
            });
            formData.append("name", values.name || "");
            formData.append("description", values.description || "");
            const res = await axios.patch(`${API_URL}/properties/${propertyId}`,
                formData,
                {
                    withCredentials: true,
                })
            toast({
                title: "Success",
                description: "Property edited successfully.",
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
                description: "Failed to edit property.",
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
                    Edit Property
                </h1>
            </div>
            {
                isLoading ? <Spinner /> :
                    <PropertyForm
                        form={form}
                        onSubmit={onSubmit}
                        fileInputRef={fileInputRef}
                        loading={loading}
                        editProperty={true}
                    />
            }
        </>
    )
}
