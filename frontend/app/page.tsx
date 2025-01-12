'use client';
import {
    Sidebar,
} from "lucide-react"

import { ModeToggle } from "@/components/mode-toggle"
import { SheetContent, SheetTrigger, Sheet } from "@/components/ui/sheet"
import NavContent from "@/components/nav/nav-content"
import ChatPage from "@/components/chat/ChatPage"
import { Button } from "@/components/ui/button"
import axios from "axios";
import { useToast } from "@/components/ui/use-toast";

export default function Dashboard() {
    const { toast } = useToast();
    const handleLogin = async () => {
        try {
            const response = await axios.post("http://localhost:8080/auth/login",
                {
                    email: "t@t.com",
                    password: "test1234",
                },
                {
                    withCredentials: true
                }
            )
            toast({
                title: "Success",
                description: response.data.message,
            })
        } catch (error: any) {
            toast({
                title: "Error",
                description: error.response.data.error,
                variant: "destructive"
            })
        }
    }
    return (
        <div className="flex flex-col dark:bg-darkSecondary w-full">
            <header className="sticky flex items-center justify-between shadow-md dark:shadow-none px-4 py-2 dark:border-none">
                <Sheet>
                    <SheetTrigger className="sm:hidden">
                        <Sidebar className="size-5" />
                    </SheetTrigger>
                    <SheetContent side="left" className="w-64 dark:bg-darkSecondary border-r-0 px-4 py-2">
                        <NavContent />
                    </SheetContent>
                </Sheet>
                <h1 className="text-xl font-semibold">Playground</h1>
                <Button className="hidden sm:block" onClick={handleLogin}>Login</Button>
                <ModeToggle />
            </header>
            <ChatPage />
        </div >
    )
}
