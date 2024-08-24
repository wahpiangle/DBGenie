import {
    CornerDownLeft,
    Mic,
    Paperclip,
    Sidebar,
} from "lucide-react"

import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from "@/components/ui/tooltip"
import { ModeToggle } from "@/components/mode-toggle"
import { Input } from "@/components/ui/input"
import { SheetContent, SheetTrigger, Sheet } from "@/components/ui/sheet"
import NavContent from "@/components/nav/nav-content"

export default function Dashboard() {
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
                <ModeToggle />
            </header>
            <main className="flex-1 gap-4 overflow-auto p-4">
                <div className="flex h-full min-h-[50vh] flex-col rounded-xl 0 p-4 lg:col-span-2 ">
                    <div className="flex-1 justify-center flex items-center">
                        Ask me anything!
                    </div>
                    <div
                        className="relative overflow-hidden rounded-lg border dark:border-none focus-within:ring-1 dark:focus-within:ring-1 dark:ring-white"
                    >
                        <Label htmlFor="message" className="sr-only">
                            Message
                        </Label>
                        <Input
                            id="message"
                            placeholder="Type your message here..."
                            className="min-h-12 p-3 shadow-none rounded-b-none dark:bg-darkTertiary border-none focus:border-none transition-none"
                        />
                        <div className="flex items-center p-3 pt-0 dark:bg-darkTertiary">
                            <Tooltip>
                                <TooltipTrigger asChild>
                                    <Button variant="ghost" size="icon">
                                        <Paperclip className="size-4" />
                                        <span className="sr-only">Attach file</span>
                                    </Button>
                                </TooltipTrigger>
                                <TooltipContent side="top">Attach File</TooltipContent>
                            </Tooltip>
                            <Tooltip>
                                <TooltipTrigger asChild>
                                    <Button variant="ghost" size="icon">
                                        <Mic className="size-4" />
                                        <span className="sr-only">Use Microphone</span>
                                    </Button>
                                </TooltipTrigger>
                                <TooltipContent side="top">Use Microphone</TooltipContent>
                            </Tooltip>
                            <Button type="button" size="sm" className="ml-auto gap-1.5">
                                Send Message
                                <CornerDownLeft className="size-3.5" />
                            </Button>
                        </div>
                    </div>
                </div>
            </main>
        </div >
    )
}
