'use client'
import { toast } from "@/components/ui/use-toast";
import API_URL from "@/constants";
import { Role } from "@/types/role";
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
    register: (
        email: string,
        password: string,
        name: string,
        role: Role
    ) => Promise<any>;
    loading: boolean;
}

export const AuthContext = createContext<AuthContextType>({
    user: null,
    login: (
        email: string,
        password: string
    ) => Promise.resolve(null),
    logout: () => { },
    register: (
        email: string,
        password: string,
        name: string,
        role: Role
    ) => Promise.resolve(null),
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

    const authRequest = async (
        url: string,
        data: {
            email: string;
            password: string;
            name?: string;
            role?: Role;
        }) => {
        try {
            setLoading(true);
            const response = await axios.post(url, data, { withCredentials: true });
            toast({ title: "Success", description: response.data.message });
            setUser(response.data);
            router.push("/");
            return response.data;
        } catch (error: any) {
            toast({
                title: "Error",
                description: error.response?.data?.error || "An error occurred.",
                variant: "destructive"
            });
        } finally {
            setLoading(false);
        }
    };

    const handleLogin = (email: string, password: string) => authRequest(`${API_URL}/auth/login`, { email, password });
    const handleRegister = (email: string, password: string, name: string, role: Role) => authRequest(
        `${API_URL}/auth/register`, { email, password, name, role });

    const handleLogout = async () => {
        try {
            setLoading(true);
            const response = await axios.get(`${API_URL}/auth/logout`, { withCredentials: true });
            toast({ title: "Success", description: response.data.message });
            setUser(null);
            router.push("/login");
        } catch (error: any) {
            toast({
                title: "Error",
                description: error.response?.data?.error || "An error occurred.",
                variant: "destructive"
            });
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        setLoading(true);
        axios.get(`${API_URL}/auth`, { withCredentials: true })
            .then((response) => setUser(response.data))
            .catch(() => setUser(null))
            .finally(() => setLoading(false));
    }, []);

    return (
        <AuthContext.Provider value={{ user, login: handleLogin, logout: handleLogout, register: handleRegister, loading }}>
            {children}
        </AuthContext.Provider>
    );

}