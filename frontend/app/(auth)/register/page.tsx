'use client';
import RegisterForm from "@/components/auth/register-form";
import withAuth from "@/components/hoc/withAuth";

export const RegisterPage = () => {
    return <RegisterForm />
}

export default withAuth(RegisterPage, { requireAuth: false });