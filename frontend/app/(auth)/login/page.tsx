'use client'
import { LoginForm } from "@/components/auth/login-form";
import withAuth from "@/components/hoc/withAuth";

const LoginPage = () => {
    return (
        <LoginForm />
    )
}

export default withAuth(LoginPage, { requireAuth: false });