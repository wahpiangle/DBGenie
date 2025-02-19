'use client';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { BookCheck, MoreVertical, Pencil, Trash } from "lucide-react"
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog"
import { useState } from "react";
import API_URL from "@/constants";
import axios from "axios";
import { toast } from "../ui/use-toast";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function BookingDropdownMenu({ bookingId }: { bookingId: string }) {
    const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false)
    const router = useRouter();
    const handleDelete = async (bookingId: string) => {
        try {
            const res = await axios.delete(`${API_URL}/bookings/${bookingId}`, {
                withCredentials: true
            })
            // router.push('/bookings')
            toast({
                title: "Success",
                description: "Property deleted successfully.",
            })
        } catch (error: any) {
            toast({
                title: "Error",
                description: "Failed to delete property.",
                variant: "destructive",
            })
        }
    }

    return (
        <>
            <DropdownMenu modal={false}>
                <DropdownMenuTrigger className="pointer-events-auto dark:hover:bg-darkTertiaryBright rounded-full p-2">
                    <MoreVertical />
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                    <Link href={`/bookings/${bookingId}/edit`}>
                        <DropdownMenuItem>
                            <Pencil className="w-4" />
                            <DropdownMenuLabel>
                                Edit Booking
                            </DropdownMenuLabel>
                        </DropdownMenuItem>
                    </Link>
                    <DropdownMenuItem onClick={() => setIsDeleteDialogOpen(true)}>
                        <div className="flex items-center gap-1 text-red-600">
                            <Trash className="w-4" />
                            <DropdownMenuLabel>
                                Delete Booking
                            </DropdownMenuLabel>
                        </div>
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu >

            <AlertDialog open={isDeleteDialogOpen}
                onOpenChange={() => setIsDeleteDialogOpen(!isDeleteDialogOpen)}

            >
                <AlertDialogTrigger asChild>
                </AlertDialogTrigger>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Delete</AlertDialogTitle>
                        <AlertDialogDescription>
                            Are you sure you want to delete this booking?
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel>Cancel</AlertDialogCancel>
                        <AlertDialogAction
                            onClick={() => { handleDelete(bookingId) }}
                        >Delete</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </>

    )
}
