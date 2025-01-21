'use client'
import { toast } from "@/components/ui/use-toast";
import axios from "axios";
import { useRouter } from "next/navigation";
import { createContext, useEffect, useState } from "react";

interface User {
    name: string;
    email: string;
    role: string;
    verified: boolean;
}

interface AuthContextType {
    user: User | null;
    login: (
        email: string,
        password: string
    ) => Promise<any>;
    logout: () => void;
    loading: boolean;
}

export const AuthContext = createContext<AuthContextType>({
    user: null,
    login: (
        email: string,
        password: string
    ) => Promise.resolve(null),
    logout: () => { },
    loading: true
});

export default function AuthProvider({
    children
}: {
    children: React.ReactNode;
}) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    const handleLogin = async (
        email: string,
        password: string
    ) => {
        try {
            setLoading(true);
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
            router.push("/");
            return response.data;
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
        } finally {
            setLoading(false);
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
            router.push("/login");
        } catch (error: any) {
            toast({
                title: "Error",
                description: error.response.data.error,
                variant: "destructive"
            })
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        setLoading(true);
        axios.get("http://localhost:8080/auth",
            {
                withCredentials: true
            }
        )
            .then((response) => {
                setUser(response.data);
            })
            .catch((error) => {
                setUser(null);
            })
            .finally(() => {
                setLoading(false);
            })

    }, [])

    return <AuthContext.Provider
        value={{
            user: user,
            login: handleLogin,
            logout: handleLogout,
            loading: loading
        }}>
        {children}
    </ AuthContext.Provider>
}