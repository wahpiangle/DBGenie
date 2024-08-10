import React from 'react'
import { Sheet, SheetContent, SheetTrigger } from '../ui/sheet'
import { PanelLeft, Home, ShoppingCart } from 'lucide-react'
import Image from 'next/image'
import { Button } from '../ui/button'
import UserAvatar from './UserAvatar'
import Link from 'next/link'

const HeaderBar = () => {
    const links: { name: string, icon: React.ReactNode, href: string }[] = [
        {
            name: 'Dashboard',
            icon: <Home className="h-5 w-5" />,
            href: '#',
        },
        {
            name: 'Orders',
            icon: <ShoppingCart className="h-5 w-5" />,
            href: '#',
        },
    ]
    return (
        <header className="sticky top-0 z-30 flex items-center border-b bg-background px-4 py-2 sm:static sm:h-auto sm:px-6 border-gray-700  bg-primary justify-between">
            <div className='flex gap-3'>
                <Sheet>
                    <SheetTrigger asChild>
                        <Button size="icon" variant="outline" className="sm:hidden bg-primary border-primary-bright hover:bg-primary-bright">
                            <PanelLeft className="h-5 w-5 text-secondary" />
                            <span className="sr-only">Toggle Menu</span>
                        </Button>
                    </SheetTrigger>
                    <SheetContent side="left" className="sm:max-w-xs bg-primary border-primary-bright">
                        <nav className="grid gap-6 text-lg font-medium">
                            <Link
                                href="#"
                                className="group flex h-10 w-10 shrink-0 items-center justify-center gap-2 rounded-full text-lg font-semibold text-primary-foreground md:text-base"
                            >
                                <Image src="/images/fittrack-transparent.png" alt="Logo" width={60} height={60} className="hover:scale-110 transition" />
                            </Link>
                            {links.map((link, index) => (
                                <Link
                                    key={index}
                                    href={link.href}
                                    className='flex items-center gap-4 px-2.5 text-gray-400 hover:text-white'
                                >
                                    {link.icon}
                                    {link.name}
                                </Link>
                            ))
                            }
                        </nav>
                    </SheetContent>
                </Sheet>
                <Link href="/">
                    <Image src="/images/fittrack-transparent.png" alt="Logo" width={40} height={40} />
                </Link>
                <div className='items-center hidden sm:flex gap-4 ml-2'>
                    {links.map((link, index) => (
                        <Link
                            key={index}
                            href={link.href}
                            className='text-sm text-gray-400 hover:text-secondary'
                        >
                            {link.name}
                        </Link>
                    ))
                    }
                </div>
            </div>

            <UserAvatar />
        </header>
    )
}

export default HeaderBar