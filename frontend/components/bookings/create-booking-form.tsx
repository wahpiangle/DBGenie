'use client';

import API_URL from "@/lib/constants";
import { createBookingSchema } from "@/schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { usePathname, useRouter } from "next/navigation";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { toast } from "../ui/use-toast";
import { ArrowLeftCircle, CalendarIcon } from "lucide-react";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "../ui/form";
import { Button } from "../ui/button";
import { Spinner } from "../ui/spinner";
import { Input } from "../ui/input";
import { Popover, PopoverContent, PopoverTrigger } from "../ui/popover";
import { format } from "date-fns";
import { Calendar } from "../ui/calendar";
import { cn } from "@/lib/utils";

export default function CreateBookingForm() {
    const [loading, setLoading] = useState(false);
    const router = useRouter();
    const propertyId = usePathname().split("/")[2];
    const form = useForm<z.infer<typeof createBookingSchema>>({
        mode: "onChange",
        resolver: zodResolver(createBookingSchema),
        defaultValues: {
            date: {
                from: new Date(),
                to: new Date(),
            },
            remarks: "",
            rentalPrice: 0,
            rentCollectionDay: 1,
            tenantEmail: "",
        },
    })

    async function onSubmit(values: z.infer<typeof createBookingSchema>) {
        try {
            setLoading(true)
            const res = await axios.post(`${API_URL}/bookings`,
                {
                    propertyId,
                    checkIn: values.date.from,
                    checkOut: values.date.to,
                    tenantEmail: values.tenantEmail,
                    rentalPrice: values.rentalPrice,
                    rentCollectionDay: values.rentCollectionDay,
                    remarks: values.remarks,
                },
                {
                    withCredentials: true,
                })
            toast({
                title: "Success",
                description: "Booking created successfully.",
            })
            form.reset()
            window.history.back()
        } catch (error: any) {
            toast({
                title: "Error",
                description: error.response.data.error,
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
                    Create Booking
                </h1>
            </div>
            <Form {...form} >
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                    <FormField
                        control={form.control}
                        name="tenantEmail"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Tenant Email</FormLabel>
                                <FormControl>
                                    <Input placeholder="john@example.com" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="date"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Check In / Check Out dates</FormLabel>
                                <FormControl>
                                    <div className="grid gap-2">
                                        <Popover>
                                            <PopoverTrigger asChild>
                                                <Button
                                                    id="date"
                                                    variant="ghost"
                                                    className={cn(
                                                        "w-full justify-start text-left font-normal outline-1 outline",
                                                        !field.value && "text-muted-foreground"
                                                    )}
                                                >
                                                    <CalendarIcon className="mr-2" />
                                                    {field.value?.from ? (
                                                        field.value.to ? (
                                                            <>
                                                                {format(field.value.from, "LLL dd, y")} -{" "}
                                                                {format(field.value.to, "LLL dd, y")}
                                                            </>
                                                        ) : (
                                                            format(field.value.from, "LLL dd, y")
                                                        )
                                                    ) : (
                                                        <span>Pick a date</span>
                                                    )}
                                                </Button>
                                            </PopoverTrigger>
                                            <PopoverContent className="w-auto p-0" align="start">
                                                <Calendar
                                                    initialFocus
                                                    mode="range"
                                                    defaultMonth={field.value?.from}
                                                    selected={field.value}
                                                    onSelect={field.onChange}
                                                    numberOfMonths={2}
                                                />
                                            </PopoverContent>
                                        </Popover>
                                    </div>
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="rentalPrice"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Rental Price in $ per month</FormLabel>
                                <FormControl>
                                    <Input placeholder="0" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="rentCollectionDay"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Rent Collection Day</FormLabel>
                                <FormControl>
                                    <Input placeholder="1-28" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="remarks"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Remarks</FormLabel>
                                <FormControl>
                                    <Input placeholder="Remarks" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <Button type="submit" disabled={loading}>
                        {loading ? <Spinner /> :
                            "Create Booking"
                        }
                    </Button>
                </form>
            </Form>
        </>
    )
}
