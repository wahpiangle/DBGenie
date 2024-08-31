import {
    Sidebar,
} from "lucide-react"

import { ModeToggle } from "@/components/mode-toggle"
import { SheetContent, SheetTrigger, Sheet } from "@/components/ui/sheet"
import NavContent from "@/components/nav/nav-content"
import Chatbox from "@/components/chatbox/chatbox"

export default function Dashboard() {
    return (
        <div className="flex flex-col dark:bg-darkSecondary  w-full">
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
                <ModeToggle />
            </header>
            <main className="flex-1 gap-4 overflow-auto p-4">
                <div className="flex h-full min-h-[50vh] flex-col rounded-xl 0 p-4 lg:col-span-2 ">
                    <div className="flex-1 justify-center flex items-center flex-col gap-8">
                        <h2 className="text-2xl">Ask me anything!</h2>
                        <Chatbox />
                    </div>
                </div>
            </main>
        </div >
    )
}
