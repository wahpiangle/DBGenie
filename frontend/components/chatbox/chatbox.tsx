"use client";
import {
    CornerDownLeft,
    SendHorizonalIcon,
} from "lucide-react"

import { Button } from "@/components/ui/button"

import { Input } from "@/components/ui/input"
import { useState } from "react";

export default function Chatbox() {
    const [inputText, setInputText] = useState('')
    const handleInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        setInputText(e.target.value)
    }
    return (
        <div
            className="flex p-4 dark:border-none border md:w-9/12 w-11/12 rounded-lg dark:bg-darkTertiary items-center gap-2 justify-between"
        >
            <Input
                id="message"
                placeholder="Type your message here..."
                className={`shadow-none dark:bg-darkSecondary border-none focus:border-none transition-none
                    `}
                onInput={handleInput}
            />
            <Button type="button" className="">
                <SendHorizonalIcon className="size-4" />
                Send
            </Button>
        </div>
    )
}
