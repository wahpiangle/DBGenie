'use client'
import { DataTableSection } from '@/components/database/data-table-section';
import withAuth from '@/components/hoc/withAuth';
import { Spinner } from '@/components/ui/spinner';
import { toast } from '@/components/ui/use-toast';
import API_URL from '@/constants';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import React from 'react'
const TablePage = () => {
    const [selectedTable, setSelectedTable] = React.useState('property')
    const tableInfo = useQuery({
        queryKey: ['table-info', selectedTable],
        queryFn: async () => {
            return await axios.get(`${API_URL}/database/get-table-info?tableName=${selectedTable}`, {
                withCredentials: true
            });
        }
    })
    const deleteRow = async (selectedTable: string, id: string) => {
        try {
            await axios.delete(`${API_URL}/database/delete-row?tableName=${selectedTable}&id=${id}`,
                {
                    withCredentials: true
                }
            );
            tableInfo.refetch();
        } catch (error) {
            toast({
                title: "Error",
                description: "Failed to delete row.",
                variant: "destructive",
            })
        }
    }

    return (
        <>
            <div className="p-4 h-screen overflow-auto">
                {tableInfo.isLoading ? (
                    <Spinner className='h-screen' />
                ) : (
                    <DataTableSection
                        data={tableInfo.error ? [] : tableInfo.data!.data.rows}
                        fields={tableInfo.error ? [] : tableInfo.data!.data.fields}
                        tables={tableInfo.error ? [] : tableInfo.data!.data.tables.map((table: any) => table.table_name)}
                        onChangeTable={(table: string) => setSelectedTable(table)}
                        selectedTable={selectedTable}
                        deleteRow={deleteRow}
                    />
                )}
            </div>
        </>
    )
}
export default withAuth(TablePage);