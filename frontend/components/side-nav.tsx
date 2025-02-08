import React from 'react'
import NavContent from './nav/nav-content'

export default function SideNav() {
    return (
        <nav className="hidden h-screen flex-col p-4 dark:bg-darkPrimary dark:text-white sm:flex">
            <NavContent />
        </nav>
    )
}
