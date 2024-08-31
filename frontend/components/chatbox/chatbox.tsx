"use client";
import {
    CornerDownLeft,
} from "lucide-react"

import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"

import { Input } from "@/components/ui/input"
import { AttachFileButton } from "./attach-file-btn"
import RecordAudioButton from "./record-audio-btn";

export default function Chatbox() {
    return (
        <div
            className="dark:border-none border focus-within:ring-1 dark:focus-within:ring-1 dark:ring-gray-500 md:w-9/12 w-11/12 rounded-lg"
        >
            <Label htmlFor="message" className="sr-only">
                Message
            </Label>
            <Input
                id="message"
                placeholder="Type your message here..."
                className="min-h-12 p-3 shadow-none rounded-b-none dark:bg-darkTertiary border-none focus:border-none transition-none"
            />
            <div className="flex items-center p-3 pt-0 dark:bg-darkTertiary rounded-b-lg">
                <AttachFileButton />
                <RecordAudioButton />
                <Button type="button" size="sm" className="ml-auto gap-1.5">
                    Send Message
                    <CornerDownLeft className="size-3.5" />
                </Button>
            </div>
        </div>
    )
}
