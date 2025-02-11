'use client'
import { SubmitHandler, UseFormReturn } from 'react-hook-form'
import { Button } from '../ui/button'
import { FormField, FormItem, FormLabel, FormControl, FormMessage, Form } from '../ui/form'
import { Spinner } from '../ui/spinner'
import { Input } from '../ui/input'

export default function PropertyForm(
    {
        form,
        onSubmit,
        fileInputRef,
        loading,
        editProperty = false
    }: {
        form: UseFormReturn<{
            name: string;
            description: string;
            imageFiles: File[];
        }, any, undefined>,
        onSubmit: SubmitHandler<{ name: string; description: string; imageFiles: File[]; }>,
        fileInputRef: React.RefObject<HTMLInputElement>,
        loading: boolean,
        editProperty?: boolean
    }
) {
    return (
        <Form {...form} >
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                <FormField
                    control={form.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Property Name</FormLabel>
                            <FormControl>
                                <Input placeholder="Property Name" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="description"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Property Description</FormLabel>
                            <FormControl>
                                <Input placeholder="Property Description" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="imageFiles"
                    render={({ field: { value, onChange, ...fieldProps } }) => (
                        <FormItem>
                            <FormLabel>Picture</FormLabel>
                            <FormControl>
                                <Input
                                    {...fieldProps}
                                    placeholder="Picture"
                                    type="file"
                                    ref={fileInputRef}
                                    accept="image/*, application/pdf"
                                    multiple
                                    onChange={(event) =>
                                        onChange(
                                            Array.from(event.target.files || []).map(
                                                (file) => file
                                            )
                                        )
                                    }
                                />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <Button type="submit" disabled={loading}>
                    {loading ? <Spinner /> :
                        editProperty ? "Update Property" : "Create Property"
                    }
                </Button>
            </form>
        </Form>
    )
}
