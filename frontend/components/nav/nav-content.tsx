import React from 'react'
import { Button } from '../ui/button'
import { ScrollArea } from '../ui/scroll-area'
import ChatButton from '../chat-button'
import { MessageCircle, Table, Triangle } from 'lucide-react'

export default function NavContent() {
    const mockData = [
        {
            title: "Chat",
            icon: <MessageCircle />,
            link: "/",
        },
        {
            title: "Database",
            icon: <Table />,
            link: "/database",
        }
        // {
        //     title: "Properties",
        //     icon: <House />,
        //     link: "/properties",
        // }
    ]
    return (
        <>
            <Button variant="outline" size="icon" aria-label="Home">
                <Triangle className="size-5 fill-foreground" />
            </Button>
            <ScrollArea className="mt-2">
                <div className="flex flex-col gap-1">
                    {mockData.map((item) => (
                        <ChatButton item={item} key={item.link} />
                    ))}
                </div>
            </ScrollArea>
        </>

    )
}
