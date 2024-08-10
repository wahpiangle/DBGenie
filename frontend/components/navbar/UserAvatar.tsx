import { Avatar, AvatarImage, AvatarFallback } from '@/components/ui/avatar'
import { DropdownMenu, DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuShortcut, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { User } from 'lucide-react'
import Link from 'next/link'
import React from 'react'
import SignOutButton from './SignOutButton'

export default function UserAvatar() {
    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Avatar className='cursor-pointer'>
                    <AvatarImage src={""} />
                    <AvatarFallback className='text-black'>
                        <User className="h-4 w-4" />
                    </AvatarFallback>
                </Avatar>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-56 bg-primary text-white border-primary-bright">
                <DropdownMenuLabel>{"displayName"}</DropdownMenuLabel>
                <DropdownMenuSeparator className='bg-primary-bright' />
                <DropdownMenuGroup>
                    <Link href="/profile">
                        <DropdownMenuItem>
                            <User className="mr-2 h-4 w-4" />
                            <span>Profile</span>
                        </DropdownMenuItem>
                    </Link>
                    <SignOutButton />
                </DropdownMenuGroup>
            </DropdownMenuContent>
        </DropdownMenu >
    )
}
