'use client';
import Chatbox from '../chatbox/chatbox'
import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import axios from "axios";

export default function ChatPage() {
    const [inputText, setInputText] = useState('')
    const useChat = useMutation({
        mutationFn: (input: String) => {
            return axios.post("http://localhost:8080/cha", {
                inputText: input
            },
                {
                    withCredentials: true,
                }
            )
        }
    })
    const handleSubmit = () => {
        setInputText('')
        useChat.mutate(inputText)
    }
    return (
        <main className="flex-1 gap-4 overflow-auto p-4">
            <div className="flex h-full min-h-[50vh] flex-col rounded-xl 0 p-4 lg:col-span-2 ">
                <div className="flex-1 justify-center flex items-center flex-col gap-8">
                    <h2 className="text-2xl">Ask me anything!</h2>
                    <Chatbox
                        inputText={inputText}
                        setInputText={setInputText}
                        handleSubmit={handleSubmit}
                    />
                </div>
            </div>
        </main>
    )
}
