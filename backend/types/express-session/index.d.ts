import 'express-session';
declare module 'express-session' {
    export interface Session {
        user: {
            id: string;
            name: string;
            email: string;
            role: string;
        }
    }
}