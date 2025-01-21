import { cn } from "@/lib/utils";
import { Poppins } from "next/font/google";

const font = Poppins({
    subsets: ["latin"],
    weight: ["500"],
})

interface HeaderProps {
    label: string;
}

export const Header = ({ label }: HeaderProps) => {
    return (
        <div className={cn("w-full flex flex-col gap-y-4 items-center py-3", font)}>
            <h1 className={cn(
                "text-3xl font-semibold",
                font.className
            )}>
                🔐 Auth
            </h1>
            <p className="text-muted-foreground text-sm py-2">
                {label}
            </p>
        </div>
    )
}