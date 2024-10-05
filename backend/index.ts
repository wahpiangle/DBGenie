import express from "express";
import session from 'express-session';
import { PrismaSessionStore } from '@quixo3/prisma-session-store';
import { prisma } from "./prisma";
import authRoute from './routes/authRoutes';

const app = express();
const port = 8080;
app.use(express.json())
app.use(express.urlencoded({ extended: true }))
app.use(
    session({
        secret: 'your-secret-key',
        resave: true,
        saveUninitialized: true,
        cookie: {
            maxAge: 7 * 24 * 60 * 60 * 1000
        },
        store: new PrismaSessionStore(
            prisma,
            {
                checkPeriod: 2 * 60 * 1000,
                dbRecordIdIsSessionId: true,
                dbRecordIdFunction: undefined,
            }
        )
    })
)

app.use('/auth', authRoute)

app.listen(port, () => {
    console.log(`Listening on port ${port}...`);
});