"use client"

import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
} from "../ui/card";
import { BackButton } from "./back-button";

interface CardWrapperProps {
    children: React.ReactNode;
    headerLabel: string;
    backButtonLabel: string;
    backButtonHref: string;
}

export const CardWrapper = ({
    children,
    headerLabel,
    backButtonLabel,
    backButtonHref,
}: CardWrapperProps) => {
    return (
        <Card className="shadow-md min-w-[400px] w-fit">
            <CardHeader className="text-center text-lg font-bold">
                {headerLabel}
            </CardHeader>
            <CardContent>
                {children}
            </CardContent>
            <CardFooter>
                <BackButton
                    label={backButtonLabel}
                    href={backButtonHref}
                >

                </BackButton>
            </CardFooter>
        </Card>
    )
}