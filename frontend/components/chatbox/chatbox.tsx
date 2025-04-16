"use client";
import {
    SendHorizonalIcon,
} from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"


export default function Chatbox(
    {
        inputText,
        setInputText,
        handleSubmit
    }: {
        inputText: string,
        setInputText: (input: string) => void,
        handleSubmit: () => void
    }
) {
    return (
        <div
            className="flex p-4 mx-1 dark:border-none border md:w-full w-11/12 rounded-lg dark:bg-darkTertiary items-center gap-2 justify-between"
        >
            <Input
                id="message"
                placeholder="Type your message here..."
                className={`shadow-none dark:bg-darkSecondary border-none focus:border-none transition-none`}
                value={inputText}
                onInput={(e) => setInputText(e.currentTarget.value)}
                onKeyDown={(e) => {
                    if (e.key === "Enter") {
                        handleSubmit()
                    }
                }}
            />
            <Button type="button" className="gap-1" onClick={() => handleSubmit()}>
                <SendHorizonalIcon className="size-4" />
                Send
            </Button>
        </div>
    )
}
