'use client';
import Chatbox from '../chatbox/chatbox'
import { useState } from "react";
import { useMutation, useQuery } from "@tanstack/react-query";
import axios from "axios";
import { toast } from '../ui/use-toast';
import { useRouter } from 'next/navigation';
import { ChatMessage } from '@/types/types';
import MessageDialog from './message-dialog';
import API_URL from '@/constants';

export default function ChatPage() {
    const [inputText, setInputText] = useState('')
    const [chatHistory, setChatHistory] = useState<ChatMessage[]>([])
    const router = useRouter();
    const chatHistoryQuery = useQuery({
        queryKey: ['chatHistory'],
        queryFn: async () => {
            const response = await axios.get(`${API_URL}/chat`, {
                withCredentials: true,
            })
            return response.data
        },
    })
    console.log("Chat history query:", chatHistoryQuery.data);
    const useChat = useMutation({
        mutationFn: (input: String) => {
            return axios.post("http://localhost:8080/chat", {
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
                { message: response.data.response, fromServer: true }
            ]);
        } catch (error: any) {
            if (error.status === 401) {
                router.push('/login')
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
        <main className="flex-1 gap-4 overflow-auto p-4 ">
            <div className="flex flex-col rounded-xl 0 p-8 lg:col-span-2 h-full justify-between gap-4">
                <div className="flex flex-col gap-4 max-w-full overflow-auto">
                    {
                        chatHistory.length === 0 && (
                            <h2 className="text-2xl text-center">Ask me anything!</h2>
                        )
                    }
                    {chatHistory.map((chat, index) => (
                        <MessageDialog
                            key={index}
                            chat={chat}
                        />
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
