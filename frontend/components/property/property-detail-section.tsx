'use client'
import API_URL from '@/constants'
import { useQuery } from '@tanstack/react-query'
import axios from 'axios'
import { useParams } from 'next/navigation'
import { Spinner } from '../ui/spinner'
import { ArrowLeftCircle } from 'lucide-react'
import ImageCarousel from './image-carousel'
import PropertyDropdownMenu from './property-dropdown-menu'

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
        <>
            {isLoading ? <Spinner />
                : isError ? <p>{error.message}</p> :
                    (
                        <>
                            <div className='mb-8 flex justify-between'>
                                <div className="flex items-center gap-2">
                                    <ArrowLeftCircle className='cursor-pointer hover:scale-110' onClick={() => window.history.back()} />
                                    <h1 className='text-2xl font-semibold'>
                                        {data ? data.name : ''}
                                    </h1>
                                </div>
                                <PropertyDropdownMenu propertyId={data.id} />
                            </div>
                            <div className='w-full'>
                                <div className='flex w-full justify-center'>
                                    <ImageCarousel imageUrls={data.image_url} />
                                </div>
                                <p>{data.description}</p>
                            </div>
                        </>
                    )}
        </>
    )
}
