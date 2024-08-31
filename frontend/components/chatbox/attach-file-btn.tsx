"use client";
import {
    Paperclip,
} from "lucide-react"

import { Button } from "@/components/ui/button"
import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from "@/components/ui/tooltip"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Input } from "../ui/input"

export function AttachFileButton() {
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        console.log(e.target.files)
    }

    return (
        <Popover>
            <Tooltip>
                <PopoverTrigger asChild>
                    <TooltipTrigger asChild>
                        <Button size="icon" variant="ghost" className="dark:hover:bg-darkSecondary">
                            <Paperclip className="size-4" />
                            <span className="sr-only">Attach file</span>
                        </Button>
                    </TooltipTrigger>
                </PopoverTrigger>
                <TooltipContent side="top">Attach File</TooltipContent>
            </Tooltip>
            <PopoverContent side="top" className="p-2 rounded-lg dark:bg-darkTertiary border dark:border-[.5px] shadow-lg dark:border-gray-500">
                <Button className="w-full flex gap-2 justify-start dark:bg-darkTertiary border-none" variant="ghost">
                    <Input type="file" className="border-none focus:border-none transition-none" onChange={handleFileChange} />
                </Button>
            </PopoverContent>
        </Popover>
    )
}
