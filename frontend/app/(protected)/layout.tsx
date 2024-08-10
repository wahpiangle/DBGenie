import { notFound } from "next/navigation";
import React from "react"
import Navbar from "@/components/navbar/navbar";
import HeaderBar from "@/components/navbar/header-bar";

const ProtectedLayout = async ({ children }: { children: React.ReactNode }) => {
    return (
        <div className="flex min-h-screen w-full flex-col" >
            <div className="flex flex-col">
                <HeaderBar />
                {children}
            </div >
        </div >
    )
}

export default ProtectedLayout