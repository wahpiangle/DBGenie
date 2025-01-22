export default function AuthLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div className="w-full flex justify-center items-center h-screen">
            {children}
        </div>
    );
}
