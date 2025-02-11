'use client';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Pencil, Trash } from "lucide-react"
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

export default function PropertyDropdownMenu({ propertyId }: { propertyId: string }) {
    const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false)
    const router = useRouter();
    const handleDelete = async (propertyId: string) => {
        try {
            const res = await axios.delete(`${API_URL}/properties/${propertyId}`, {
                withCredentials: true
            })
            router.push('/properties')
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
            <DropdownMenu>
                <DropdownMenuTrigger className="pointer-events-auto bg-white text-black py-2 px-4 rounded-lg">
                    Actions
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                    <Link href={`/properties/${propertyId}/edit`}>
                        <DropdownMenuItem>
                            <Pencil className="w-4" />
                            <DropdownMenuLabel>
                                Edit Property
                            </DropdownMenuLabel>
                        </DropdownMenuItem>
                    </Link>
                    <DropdownMenuItem onClick={() => setIsDeleteDialogOpen(true)}>
                        <div className="flex items-center gap-1 text-red-600">
                            <Trash className="w-4" />
                            <DropdownMenuLabel>
                                Delete Property
                            </DropdownMenuLabel>
                        </div>
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu >

            <AlertDialog open={isDeleteDialogOpen}
                onOpenChange={() => setIsDeleteDialogOpen(!isDeleteDialogOpen)}>
                <AlertDialogTrigger asChild>
                </AlertDialogTrigger>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Delete</AlertDialogTitle>
                        <AlertDialogDescription>
                            Are you sure you want to delete this property?
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel>Cancel</AlertDialogCancel>
                        <AlertDialogAction
                            onClick={() => { handleDelete(propertyId) }}
                        >Delete</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </>

    )
}
