import SideNav from "@/components/side-nav";

export default function ProtectedLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="h-screen flex w-screen">
      <SideNav classname="w-2/12 hidden md:flex" />
      <div className="md:w-10/12 w-full bg-darkSecondary h-screen">
        {children}
      </div>
    </div>
  );
}
