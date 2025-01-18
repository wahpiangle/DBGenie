'use client'
import { toast } from "@/components/ui/use-toast";
import axios from "axios";
import { createContext, useEffect, useState } from "react";

interface User {
    id: string;
    name: string;
    email: string;
    role: string;
    verified: boolean;
}

interface AuthContextType {
    user: User | null;
    login: () => void;
    logout: () => void;
}

export const AuthContext = createContext<AuthContextType>({
    user: null,
    login: () => { },
    logout: () => { }
});

export default function AuthProvider({
    children
}: {
    children: React.ReactNode;
}) {
    const [user, setUser] = useState(null);
    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axios.get("http://localhost:8080/auth/",
                    {
                        withCredentials: true
                    }
                )
                setUser(response.data);
            } catch (error: any) {
                if (error.response) {
                    toast({
                        title: "Error",
                        description: error.response.data.message,
                        variant: "destructive"
                    })
                } else {
                    toast({
                        title: "Error",
                        description: "Failed to fetch user.",
                        variant: "destructive"
                    })
                }
                setUser(null);
            }
        }
        fetchUser();
    }, [])

    const handleLogin = async () => {
        try {
            const response = await axios.post("http://localhost:8080/auth/login",
                {
                    email: "t@t.com",
                    password: "test1234",
                },
                {
                    withCredentials: true
                }
            )
            toast({
                title: "Success",
                description: response.data.message,
            })
            setUser(response.data);
        } catch (error: any) {
            if (error.response) {
                toast({
                    title: "Error",
                    description: error.response.data.error,
                    variant: "destructive"
                })
            } else {
                toast({
                    title: "Error",
                    description: "Failed to login.",
                    variant: "destructive"
                })
            }
        }
    }

    const handleLogout = async () => {
        try {
            const response = await axios.get("http://localhost:8080/auth/logout",
                {
                    withCredentials: true
                }
            )
            toast({
                title: "Success",
                description: response.data.message,
            })
            setUser(null);
        } catch (error: any) {
            toast({
                title: "Error",
                description: error.response.data.error,
                variant: "destructive"
            })
        }
    }
    return <AuthContext.Provider
        value={{
            user: user,
            login: handleLogin,
            logout: handleLogout
        }}>
        {children}
    </ AuthContext.Provider>
}