import { type NextFunction, type Request, type Response } from 'express';

const redirectLogin = (req: Request, res: Response, next: NextFunction) => {
    if (!req.session.user) {
        res.json({ message: 'You need to be logged in to access this route' });
    } else {
        next();
    }
}

export default redirectLogin;