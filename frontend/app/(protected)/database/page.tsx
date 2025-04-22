'use client';
import DatabaseSchemaSection from '@/components/database/database-schema-section'
import withAuth from '@/components/hoc/withAuth';
import { Spinner } from '@/components/ui/spinner';
import API_URL from '@/lib/constants';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';

const DatabasePage = () => {
    const databaseInfo = useQuery({
        queryKey: ['database-info'],
        queryFn: async () => {
            return await axios.get(`${API_URL}/database`, {
                withCredentials: true
            });
        }
    })
    return (
        databaseInfo.isLoading ?
            <Spinner className='h-screen' /> :
            <DatabaseSchemaSection databaseInfo={databaseInfo.data!.data} />
    )
}
export default withAuth(DatabasePage);