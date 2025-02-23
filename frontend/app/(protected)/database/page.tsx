'use client';
import DatabaseSchemaSection from '@/components/database/database-schema-section'
import withAuth from '@/components/hoc/withAuth';
import API_URL from '@/constants';
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
            <div>Loading...</div> :
            <DatabaseSchemaSection databaseInfo={databaseInfo.data!.data} />
    )
}
export default withAuth(DatabasePage);