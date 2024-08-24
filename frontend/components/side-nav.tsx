import React from 'react'
import NavContent from './nav/nav-content'

export default function SideNav() {
    return (
        <nav className="hidden h-full flex-col p-4 w-64 dark:bg-darkPrimary dark:text-white sm:flex shadow-lg">
            <NavContent />
        </nav>
    )
}
