import SideNav from "@/components/side-nav";

export default function ProtectedLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="h-screen w-full flex">
      <SideNav />
      <div className="w-full bg-darkSecondary">
        {children}
      </div>
    </div>
  );
}
