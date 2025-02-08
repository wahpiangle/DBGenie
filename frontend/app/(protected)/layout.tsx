import SideNav from "@/components/side-nav";

export default function ProtectedLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="h-full min-h-screen flex w-screen">
      <SideNav />
      <div className="w-full flex-auto bg-darkSecondary">
        {children}
      </div>
    </div>
  );
}
