import { Carousel, CarouselContent, CarouselItem, CarouselPrevious, CarouselNext } from '../ui/carousel'
import Image from 'next/image'

export default function ImageCarousel({ imageUrls }: { imageUrls: string[] }) {
    return (
        <Carousel className="w-full max-w-3xl rounded-lg">
            <CarouselContent>
                {imageUrls.length > 0 ?
                    imageUrls.map((url, index) => (
                        <CarouselItem key={index}>
                            <div className="relative w-full h-[400px]">
                                <Image
                                    src={url}
                                    alt="Property Image"
                                    fill
                                    className="object-contain border-[1px] border-neutral-700 rounded-lg"
                                />
                            </div>
                        </CarouselItem>
                    )) :
                    <CarouselItem>
                        <img src="https://placehold.co/600x400" alt="Property Image" className="w-full h-[400px] object-contain border-[1px] border-neutral-700 rounded-lg" />
                    </CarouselItem>
                }
            </CarouselContent>
            <CarouselPrevious />
            <CarouselNext />
        </Carousel>
    )
}
