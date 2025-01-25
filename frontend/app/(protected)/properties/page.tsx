'use client';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from "@/components/ui/card";
import { Property } from "@/types/types";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { useRouter } from "next/navigation";
import { Key, ReactElement, JSXElementConstructor, ReactNode, ReactPortal, AwaitedReactNode } from "react";

export default function PropertiesPage() {
    const router = useRouter();
    const useProperties = useQuery({
        queryKey: ['properties'],
        queryFn: () => {
            return axios.get('http://localhost:8080/properties',
                {
                    withCredentials: true
                }
            )
        }
    })

    return (
        <div className="grid grid-cols-3 gap-4 p-4 w-full">
            {useProperties.data?.data.map
                ((property: Property) => (
                    <Card key={property.id}
                        onClick={() => router.push(`/properties/${property.id}`)}
                        className="cursor-pointer"
                    >
                        <CardHeader>
                            <CardTitle>{property.name}</CardTitle>
                            <CardDescription>{property.description}</CardDescription>
                        </CardHeader>
                        <CardContent>
                            <img src={property.imageUrl[0]} alt="Property Image"
                                className="w-full object-cover rounded-lg" />
                        </CardContent>
                        <CardFooter>
                            <p>Card Footer</p>
                        </CardFooter>
                    </Card>
                ))}

        </div>

    )
}
