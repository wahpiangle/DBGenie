import { CircleX } from "lucide-react"
import { Alert, AlertDescription, AlertTitle } from "../ui/alert"

interface FormErrorProps {
    message?: string
}

export const FormError = ({ message }: FormErrorProps) => {
    if (!message) return null

    return (
        <Alert variant="destructive">
            <CircleX className="w-5" />
            <AlertTitle>Error</AlertTitle>
            <AlertDescription>
                {message}
            </AlertDescription>
        </Alert>
    )
}