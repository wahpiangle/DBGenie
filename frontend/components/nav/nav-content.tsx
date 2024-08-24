import React from 'react'
import { Button } from '../ui/button'
import { ScrollArea } from '../ui/scroll-area'
import ChatButton from '../chat-button'
import { Triangle } from 'lucide-react'

export default function NavContent() {
    const mockData = [
        {
            id: 1,
            title: "Home is where asfjkdlksjaklfalasjf",
        },
        {
            id: 2,
            title: "About",
        },
        {
            id: 3,
            title: "Services",
        },
        {
            id: 4,
            title: "Contact",
        },
    ]
    return (
        <>
            <Button variant="outline" size="icon" aria-label="Home">
                <Triangle className="size-5 fill-foreground" />
            </Button>
            <ScrollArea className="mt-2">
                <div className="flex flex-col gap-1">
                    {mockData.map((item) => (
                        <ChatButton item={item} key={item.id} />
                    ))}
                </div>
            </ScrollArea>
        </>

    )
}
