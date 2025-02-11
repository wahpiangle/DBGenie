import React from 'react'

export default function ProtectedLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className="w-full p-4 flex-auto bg-darkSecondary">
            {children}
        </div>
    )
}
