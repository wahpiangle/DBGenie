import { NextResponse } from 'next/server'
import type { NextRequest } from 'next/server'

export async function middleware(request: NextRequest) {
    const session = request.cookies.get('connect.sid');
    if (session) {
        return NextResponse.next()
    } else {
        if (request.nextUrl.pathname !== '/login') {
            return NextResponse.redirect(new URL('/login', request.url))
        }
    }
}

export const config = {
    matcher: '/((?!api|_next/static|_next/image|favicon.ico).*)',
}