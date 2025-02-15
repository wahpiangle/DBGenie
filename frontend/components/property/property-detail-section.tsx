'use client'
import API_URL from '@/constants'
import { useQuery } from '@tanstack/react-query'
import axios from 'axios'
import { useParams } from 'next/navigation'
import { Spinner } from '../ui/spinner'
import { ArrowLeftCircle } from 'lucide-react'
import ImageCarousel from './image-carousel'
import PropertyDropdownMenu from './property-dropdown-menu'
import { Label } from '@radix-ui/react-label'
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '../ui/card'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../ui/tabs'
import { Input } from '../ui/input'
import { Property } from '@/types/types'

export default function PropertyDetailSection() {
    const { id } = useParams<{ id: string }>()
    const { isLoading, isError, data, error } = useQuery({
        queryKey: ['property', id],
        queryFn: async () => {
            const response = await axios.get(`${API_URL}/properties/${id}`, {
                withCredentials: true
            });
            return response.data as Property
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
                                <PropertyDropdownMenu propertyId={data!.id} />
                            </div>
                            <Tabs defaultValue="property">
                                <TabsList className="grid grid-cols-2 w-full mb-4">
                                    <TabsTrigger value="property">Property Detail</TabsTrigger>
                                    <TabsTrigger value="booking">Bookings</TabsTrigger>
                                </TabsList>
                                <TabsContent value="property">
                                    <Card>
                                        <CardHeader>
                                            <CardTitle>{data!.name}</CardTitle>
                                            <CardDescription>
                                                {data!.description}
                                            </CardDescription>
                                        </CardHeader>
                                        <CardContent className="space-y-2">
                                            <div className='w-full'>
                                                <div className='flex w-full justify-center'>
                                                    <ImageCarousel imageUrls={data!.image_url} />
                                                </div>
                                            </div>
                                        </CardContent>
                                        <CardFooter>
                                        </CardFooter>
                                    </Card>
                                </TabsContent>
                                <TabsContent value="booking">
                                    <Card>
                                        <CardHeader>
                                            <CardTitle>Bookings</CardTitle>
                                            <CardDescription>
                                            </CardDescription>
                                        </CardHeader>
                                        <CardContent className="space-y-2">
                                            {data!.bookings.map((booking) => (
                                                <div key={booking.id}>
                                                    {JSON.stringify(booking)}
                                                </div>
                                            ))}
                                        </CardContent>
                                        <CardFooter>
                                        </CardFooter>
                                    </Card>
                                </TabsContent>
                            </Tabs>


                        </>
                    )}
        </>
    )
}
