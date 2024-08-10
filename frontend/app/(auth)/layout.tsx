
import Image from 'next/image';
export default function RootLayout({
    children,
}: {
    children: React.ReactNode;
}) {
    return (
        <div className="flex items-center min-h-screen bg-primary">
            <div className="flex-[2_2_0%] flex justify-center">
                <div className="space-y-6">
                    <h1 className="text-2xl font-bold text-secondary text-center">
                        FitTrack PT Platform
                    </h1>
                    {children}
                </div>
            </div>
            <div className="flex-1 relative top-0 left-0 md:block hidden">
                <Image src="/images/herobg.png" width={1200} height={1200} alt="Logo" className="relative h-screen object-cover" />
                <Image src="/images/FitTrack.png" width={100} height={100} alt="FitTrack" className=" absolute z-10 right-0 top-0" />
            </div>
        </div>
    );
}
