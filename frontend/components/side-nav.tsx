import React from 'react'
import NavContent from './nav/nav-content'

export default function SideNav({
    classname,
}: Readonly<{
    classname: string
}>
) {
    return (
        <nav className={`h-screen flex-col p-4 dark:bg-darkPrimary dark:text-white sm:flex ${classname}`}>
            <NavContent />
        </nav>
    )
}
