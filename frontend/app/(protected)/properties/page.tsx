'use client';
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from "@/components/ui/card";
import { Spinner } from "@/components/ui/spinner";
import API_URL from "@/constants";
import { cn } from "@/lib/utils";
import { Property } from "@/types/types";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { useRouter } from "next/navigation";

export default function PropertiesPage() {
    const router = useRouter();
    const useProperties = useQuery({
        queryKey: ['properties'],
        queryFn: () => {
            return axios.get(
                `${API_URL}/properties`,
                {
                    withCredentials: true
                }
            )
        }
    })

    return (
        <div className="w-full">
            <div className="flex justify-between items-center p-4 font-bold">
                <h1 className="text-3xl">Properties</h1>
                <Button onClick={() => router.push('/properties/create')}>Create Property</Button>
            </div>
            <div className={cn("grid grid-cols-3 gap-4 p-4 w-full", useProperties.isLoading && "grid-cols-1")}>
                {useProperties.isLoading &&
                    <Spinner />
                }
                {useProperties.data?.data.map
                    ((property: Property) => (
                        <Card key={property.id}
                            onClick={() => router.push(`/properties/${property.id}`)}
                            className="cursor-pointer h-fit"
                        >
                            <CardHeader>
                                <CardTitle>{property.name}</CardTitle>
                                <CardDescription>{property.description}</CardDescription>
                            </CardHeader>
                            <CardContent className="flex justify-center">
                                <img src={property.image_url[0] ??
                                    "https://placehold.co/600x400"
                                } alt="Property Image"
                                    className="object-cover rounded-lg w-80" />
                            </CardContent>
                            <CardFooter>
                                <p>{property.bookings.length} bookings</p>
                            </CardFooter>
                        </Card>
                    ))}
            </div>
        </div>
    )
}
