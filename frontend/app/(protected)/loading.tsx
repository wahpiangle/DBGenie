import { Spinner } from '@/components/ui/spinner'
import React from 'react'

export default function ProtectedLoading() {
    return (
        <div className='justify-center items-center flex h-screen'>
            <Spinner />
        </div>
    )
}
