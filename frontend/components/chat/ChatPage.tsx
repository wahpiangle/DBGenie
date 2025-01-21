'use client';
import Chatbox from '../chatbox/chatbox'
import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import axios from "axios";
import { ChatMessage } from '@/types/ChatMessage';
import { Spinner } from '../ui/spinner';
import { toast } from '../ui/use-toast';

export default function ChatPage() {
    const [inputText, setInputText] = useState('')
    const [chatHistory, setChatHistory] = useState<ChatMessage[]>([])

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
    const handleSubmit = async () => {
        setChatHistory((prev) => [
            ...prev,
            { message: inputText, fromServer: false },
            { message: 'Loading...', fromServer: true, pending: true }
        ]);
        setInputText('');
        try {
            const response = await useChat.mutateAsync(inputText);
            setChatHistory((prev) => [
                ...prev.slice(0, -1),
                { message: response.data.message, fromServer: true }
            ]);
        } catch (error: any) {
            if (error.status === 401) {
                // TODO
                // window.location.href = '/login'
                return
            }
            toast({
                title: "Error",
                description: "Failed to fetch response.",
                variant: "destructive",
            })
            setChatHistory((prev) => [
                ...prev.slice(0, -1),
                { message: "Error fetching response.", fromServer: true, error: true }
            ]);
        }
    };

    return (
        <main className="flex-1 gap-4 overflow-auto p-4 h-full">
            <div className="flex flex-col rounded-xl 0 p-8 lg:col-span-2 h-full justify-between gap-4">
                <div className="flex flex-col gap-4 w-full overflow-auto">
                    {
                        chatHistory.length === 0 && (
                            <h2 className="text-2xl text-center">Ask me anything!</h2>
                        )
                    }
                    {chatHistory.map((chat, index) => (
                        <div key={index} className={`flex gap-2 ${chat.fromServer ? 'flex-row' : 'flex-row-reverse'}`}>
                            <div className=
                                {`px-4 py-2 rounded-xl
                                ${chat.fromServer ? 'bg-gray-200' : 'bg-blue-400'}
                                text-black`
                                }
                            >
                                {chat.pending ?
                                    <Spinner /> :
                                    chat.message
                                }
                            </div>
                        </div>
                    ))}
                </div>
                <Chatbox
                    inputText={inputText}
                    setInputText={setInputText}
                    handleSubmit={handleSubmit}
                />
            </div>
        </main>
    )
}
