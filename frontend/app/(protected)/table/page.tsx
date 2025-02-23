'use client'
import { DataTableSection } from '@/components/database/data-table-section';
import { Spinner } from '@/components/ui/spinner';
import API_URL from '@/constants';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import React from 'react'

export default function TablePage() {
    const [selectedTable, setSelectedTable] = React.useState('property')
    const tableInfo = useQuery({
        queryKey: ['table-info', selectedTable],
        queryFn: async () => {
            return await axios.get(`${API_URL}/database/get-table-info?tableName=${selectedTable}`, {
                withCredentials: true
            });
        }
    })

    return (
        <>
            {tableInfo.error ?
                <h1 className='text-2xl font-bold text-red-500'>Error</h1> :
                tableInfo.isLoading ?
                    <Spinner className='h-screen' /> :
                    <div className='p-4'>
                        <DataTableSection
                            data={tableInfo.data!.data.rows}
                            fields={tableInfo.data!.data.fields}
                            tables={tableInfo.data!.data.tables.map(
                                (table: any) => table.table_name
                            )}
                            onChangeTable={(table: string) => setSelectedTable(table)}
                            selectedTable={selectedTable}
                        />
                    </div>
            }
        </>
    )
}
