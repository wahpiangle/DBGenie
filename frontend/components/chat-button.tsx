'use client';

import { useParams } from "next/navigation";
import { Button } from "./ui/button";
import Link from "next/link";

export default function ChatButton({ item }: { item: { title: string, icon: React.ReactElement, link: string } }) {
    const params = useParams()
    return (
        <Button variant="ghost" className="w-full dark:hover:bg-darkTertiary flex gap-2" asChild>
            <Link href={item.link}>
                {item.icon}
                <span className="truncate min-w-0 w-[120px] text-left">{item.title}
                </span>
            </Link>
        </Button>
    )
}
