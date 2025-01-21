'use client';
import { Button } from "@/components/ui/button";
import { AuthContext } from "@/context/AuthContext";
import { useContext } from "react";

export default function LoginPage() {
    const { user, login, logout } = useContext(AuthContext);
    return (
        <div>
            {
                user ?
                    <Button className="hidden sm:block" onClick={logout}>Logout</Button>
                    :
                    <Button className="hidden sm:block" onClick={login}>Login</Button>
            }
        </div>
    )
}
