import { Booking } from '@/types/types'
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '../ui/card'
import { BookCheck, Link, MoreVertical, Pencil, Trash } from 'lucide-react'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuLabel, DropdownMenuTrigger } from '../ui/dropdown-menu'
import BookingDropdownMenu from './booking-dropdown-menu'

export default function BookingListSection(
    { bookings }: { bookings: Booking[] }
) {
    return (
        <div className='grid grid-cols-3 gap-4'>
            {
                bookings.map((booking) => (
                    <Card key={booking.id}>
                        <CardHeader className='flex justify-between flex-row'>
                            <div>
                                <CardTitle>{booking.user.name}</CardTitle>
                                <CardDescription>{booking.user.email}</CardDescription>
                            </div>
                            <BookingDropdownMenu bookingId={booking.id} />
                        </CardHeader>
                        <CardContent>
                            <p>
                                {new Date(booking.check_in).toLocaleDateString()} - {new Date(booking.check_out).toLocaleDateString()}
                            </p>
                        </CardContent>
                        {
                            booking.remarks && (
                                <CardFooter>
                                    <p>{booking.remarks}</p>
                                </CardFooter>
                            )
                        }
                    </Card>
                ))
            }
        </div>
    )
}
