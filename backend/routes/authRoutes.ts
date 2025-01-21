import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { AuthController } from '../controller/authController';
const router = express.Router();

router.get('/', redirectLogin, (req, res) => {
    res.send({
        email: req.session.user.email,
        name: req.session.user.name,
        role: req.session.user.role,
        verified: req.session.user.verified
    });
});

router.post('/register', AuthController.register);
router.post('/login', AuthController.login);
router.get('/logout', AuthController.logout);
router.post('/verify-account', AuthController.verifyAccount);

export default router;