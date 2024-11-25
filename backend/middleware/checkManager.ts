import { Role } from "@prisma/client";
import type { NextFunction, Request, Response } from "express"

const checkManager = (req: Request, res: Response, next: NextFunction) => {
    if (req.session.user.role !== Role.MANAGER) {
        res.status(403).json({ error: 'Unauthorized' });
        return;
    }
    req.session.resetMaxAge();
    next();
}

export default checkManager;