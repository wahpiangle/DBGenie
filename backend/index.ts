import express from "express";
import session from 'express-session';
import { PrismaSessionStore } from '@quixo3/prisma-session-store';
import { prisma } from "./prisma";
import authRoute from './routes/authRoutes';
import propertyRoute from './routes/propertyRoutes';
import bookingRoute from './routes/bookingRoutes';
import chatRoute from './routes/chatRoutes';
import maintenanceRequestRoute from './routes/maintenanceRequestRoutes';
import cors from 'cors';

const app = express();
const port = 8080;
app.use(cors({
    origin: 'http://localhost:3000',
    credentials: true,
}));

app.use(express.json())
app.use(express.urlencoded({ extended: true }))
app.use(
    session({
        secret: 'your-secret-key',
        resave: false,
        saveUninitialized: false,
        cookie: {
            maxAge: 7 * 24 * 60 * 60 * 1000,
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
app.use('/properties', propertyRoute)
app.use('/bookings', bookingRoute)
app.use('/maintenance-request', maintenanceRequestRoute)
app.use('/chat', chatRoute)
app.post('/cha', async (req, res) => {
    const { inputText } = req.body
    res.json({ message: 'Hello' })
})

app.listen(port, () => {
    console.log(`Listening on port ${port}...`);
});