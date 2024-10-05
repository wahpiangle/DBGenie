import { type NextFunction, type Request, type Response } from 'express';

const redirectLogin = (req: Request, res: Response, next: NextFunction) => {
    if (!req.session.user) {
        res.redirect('/auth/login');
    } else {
        next();
    }
}

export default redirectLogin;