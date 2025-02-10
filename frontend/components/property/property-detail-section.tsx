'use client'
import API_URL from '@/constants'
import { useQuery } from '@tanstack/react-query'
import axios from 'axios'
import { useParams } from 'next/navigation'

export default function PropertyDetailSection() {
    const { id } = useParams<{ id: string }>()
    const { isLoading, isError, data, error } = useQuery({
        queryKey: ['property', id],
        queryFn: async () => {
            const response = await axios.get(`${API_URL}/properties/${id}`, {
                withCredentials: true
            });
            return response.data
        }
    })
    return (
        <div>
            {isLoading && <p>Loading...</p>}
            {isError && <p>{error.message}</p>}
            {data && (
                <div>
                    <h1>{data.name}</h1>
                    <p>{data.description}</p>
                    <img src={data.image_url[0] ?? "https://placehold.co/600x400"} alt="Property Image" />
                </div>
            )}
        </div>
    )
}
