"use client";
import {
    CornerDownLeft,
    SendHorizonalIcon,
} from "lucide-react"

import { Button } from "@/components/ui/button"

import { Input } from "@/components/ui/input"
import { AttachFileButton } from "./attach-file-btn"
import RecordAudioButton from "./record-audio-btn";
import { useState } from "react";

export default function Chatbox() {
    const [isRecording, setIsRecording] = useState(false)
    const [hasRecording, setHasRecording] = useState(false)
    const [inputText, setInputText] = useState('')
    const handleInput = (e: React.ChangeEvent<HTMLInputElement>) => {
        setInputText(e.target.value)
    }
    return (
        <div
            className="flex p-4 dark:border-none border md:w-9/12 w-11/12 rounded-lg dark:bg-darkTertiary items-center gap-2 justify-between"
        >
            <AttachFileButton />
            <Input
                id="message"
                placeholder="Type your message here..."
                className={`shadow-none dark:bg-darkSecondary border-none focus:border-none transition-none
                        ${isRecording || hasRecording ? 'hidden' : 'block'}
                    `}
                onInput={handleInput}
            />
            {
                inputText.length === 0 ? (
                    <RecordAudioButton isRecording={isRecording} setIsRecording={setIsRecording} hasRecording={hasRecording} setHasRecording={setHasRecording} />
                ) : (
                    <Button type="button" className="">
                        <SendHorizonalIcon className="size-4" />
                        Send
                    </Button>
                )
            }
        </div>
    )
}
