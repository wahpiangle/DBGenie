import {
    Sidebar,
    TrashIcon,
} from "lucide-react"

import { SheetContent, SheetTrigger, Sheet } from "@/components/ui/sheet"
import NavContent from "@/components/nav/nav-content"
import { Button } from "@/components/ui/button"
import { useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import API_URL from "@/lib/constants";
import { toast } from "../ui/use-toast";
import { useRouter } from "next/navigation";
import { AuthContext } from "@/context/AuthContext";
import { useContext } from "react";
export default function ChatPageHeader() {

    const queryClient = useQueryClient();
    const router = useRouter();
    const { user, logout } = useContext(AuthContext);

    const handleClearChat = async () => {
        try {
            const response = await axios.get(`${API_URL}/chat/clear`, {
                withCredentials: true,
            })
            queryClient.invalidateQueries({ queryKey: ['chatHistory'] });
            toast({
                title: "Success",
                description: response.data.message,
            })
        } catch (error: any) {
            if (error.status === 401) {
                router.push('/login')
                return
            }
            toast({
                title: "Error",
                description: "Failed to fetch response.",
                variant: "destructive",
            })
        }
    }
    return (
        <header className="sticky flex items-center justify-between shadow-md dark:shadow-none px-4 py-2 dark:border-none">
            <Sheet>
                <SheetTrigger className="sm:hidden">
                    <Sidebar className="size-5" />
                </SheetTrigger>
                <SheetContent side="left" className="w-64 dark:bg-darkSecondary border-r-0 px-4 py-2">
                    <NavContent />
                </SheetContent>
            </Sheet>
            {
                user &&
                <Button className="hidden sm:block" onClick={logout}>Logout</Button>
            }
            <Button onClick={handleClearChat}><TrashIcon className="mr-2" />Clear Chat</Button>
        </header>
    )
}
