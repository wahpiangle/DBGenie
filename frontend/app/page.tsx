import {
    CornerDownLeft,
    Mic,
    Paperclip,
    Triangle,
} from "lucide-react"

import { Button } from "@/components/ui/button"

import { Label } from "@/components/ui/label"

import { Textarea } from "@/components/ui/textarea"
import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from "@/components/ui/tooltip"
import { ScrollArea } from "@/components/ui/scroll-area"
import { ModeToggle } from "@/components/mode-toggle"

export default function Dashboard() {
    return (
        <div className="h-screen w-full pl-[56px] flex">
            <nav className="inset-y fixed  left-0 z-20 flex h-full flex-col border-r p-2 text-center">
                <Button variant="outline" size="icon" aria-label="Home">
                    <Triangle className="size-5 fill-foreground" />
                </Button>
                <ScrollArea>
                    Test
                </ScrollArea>
            </nav>
            <div className="flex flex-col dark:bg-[#262626] w-full">
                <header className="sticky top-0 z-10 flex h-[57px] items-center gap-1 bg-background px-4">
                    <h1 className="text-xl font-semibold">Playground</h1>
                    <ModeToggle />
                </header>
                <main className="grid flex-1 gap-4 overflow-auto p-4">
                    <div className="relative flex h-full min-h-[50vh] flex-col rounded-xl bg-muted/50 p-4 lg:col-span-2 ">
                        <div className="flex-1" />
                        <form
                            className="relative overflow-hidden rounded-lg border bg-background focus-within:ring-1 dark:focus-within:ring-1 dark:border-[#262626] dark:ring-white"
                        >
                            <Label htmlFor="message" className="sr-only">
                                Message
                            </Label>
                            <Textarea
                                id="message"
                                placeholder="Type your message here..."
                                className="min-h-12 resize-none border-0 p-3 shadow-none focus-visible:ring-0 rounded-b-none"
                            />
                            <div className="flex items-center p-3 pt-0 dark:bg-slate-950">
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
                                <Button type="submit" size="sm" className="ml-auto gap-1.5">
                                    Send Message
                                    <CornerDownLeft className="size-3.5" />
                                </Button>
                            </div>
                        </form>
                    </div>
                </main>
            </div>
        </div>
    )
}
