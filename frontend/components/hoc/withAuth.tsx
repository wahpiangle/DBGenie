'use client';
import { AuthContext } from '@/context/AuthContext';
import { useRouter } from 'next/navigation';
import { useContext, useEffect } from 'react';
import { Spinner } from '../ui/spinner';

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
        if (loading || (!requireAuth && user)) {
            return <div className='w-screen h-screen justify-center items-center flex'>
                <Spinner />
            </div>
        }
        if (requireAuth && !user) {
            return <div className='w-full h-full justify-center items-center flex'>
                <Spinner />
            </div>
        }
        return <Component {...props} />;
    };
};

export default withAuth;
