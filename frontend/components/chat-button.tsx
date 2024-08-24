'use client';

import { useParams } from "next/navigation";
import { Button } from "./ui/button";

export default function ChatButton({ item }: { item: { id: number, title: string } }) {
    const params = useParams()
    return (
        <Button variant="ghost" className="w-full dark:hover:bg-darkTertiary">
            <span className="truncate min-w-0 w-[150px] text-left">{item.title}
            </span>
        </Button>
    )
}
