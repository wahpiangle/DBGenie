import { Spinner } from '@/components/ui/spinner'
import React from 'react'

export default function ProtectedLoading() {
    return (
        <div className='w-screen justify-center items-center flex'>
            <Spinner />
        </div>
    )
}
