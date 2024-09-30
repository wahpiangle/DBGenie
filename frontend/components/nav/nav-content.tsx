import React from 'react'
import { Button } from '../ui/button'
import { ScrollArea } from '../ui/scroll-area'
import ChatButton from '../chat-button'
import { MessageCircle, Package2, Triangle } from 'lucide-react'

export default function NavContent() {
    const mockData = [
        {
            title: "Chat",
            icon: <MessageCircle />,
            link: "/",
        },
        {
            title: "Products",
            icon: <Package2 />,
            link: "/products",
        },
    ]
    return (
        <>
            <Button variant="outline" size="icon" aria-label="Home">
                <Triangle className="size-5 fill-foreground" />
            </Button>
            <ScrollArea className="mt-2">
                <div className="flex flex-col gap-1">
                    {/* Bolded Text to categorise */}
                    {mockData.map((item) => (
                        <ChatButton item={item} key={item.link} />
                    ))}
                </div>
            </ScrollArea>
        </>

    )
}
