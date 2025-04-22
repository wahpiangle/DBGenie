'use client'
import { useContext } from 'react'
import { Button } from '../ui/button'
import { ScrollArea } from '../ui/scroll-area'
import ChatButton from '../chat-button'
import { Database, MessageCircle, Table, Triangle } from 'lucide-react'
import { AuthContext } from '@/context/AuthContext'

export default function NavContent() {
    const { user, logout } = useContext(AuthContext);
    const mockData = [
        {
            title: "Chat",
            icon: <MessageCircle />,
            link: "/",
        },
        {
            title: "Database",
            icon: <Database />,
            link: "/database",
        },
        {
            title: "Tables",
            icon: <Table />,
            link: "/table",
        }
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
                    {
                        user &&
                        <Button className="sm:hidden" onClick={logout}>Logout</Button>
                    }
                </div>
            </ScrollArea>
        </>

    )
}
