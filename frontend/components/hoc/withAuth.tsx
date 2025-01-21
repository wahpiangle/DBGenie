'use client';
import { AuthContext } from '@/context/AuthContext';
import { Loader } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { useContext, useEffect } from 'react';

const withAuth = (
    Component: React.FC,
    { requireAuth = true }:
        { requireAuth?: boolean } = {}
) => {
    return function ProtectedRoute(props: any) {
        const { user, loading } = useContext(AuthContext);
        const router = useRouter();

        useEffect(() => {
            if (requireAuth && !user) {
                router.push('/login');
            } else if (!requireAuth && user) {
                router.push('/');
            }
        }, [user, requireAuth, router]);
        console.log(user, loading, requireAuth);
        if (loading || (!requireAuth && user)) {
            return <div className='w-screen h-screen justify-center items-center flex'>
                <Loader />
            </div>
        }
        if (requireAuth && !user) {
            return <div className='w-full h-full justify-center items-center flex'>
                <Loader />
            </div>
        }
        return <Component {...props} />;
    };
};

export default withAuth;
