import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { AuthController } from '../controller/authController';
const router = express.Router();

router.get('/', redirectLogin, (req, res) => {
    res.send(`Welcome ${req.session.user.name}`);
});

router.post('/register', AuthController.register);
router.post('/login', AuthController.login);
router.get('/logout', AuthController.logout);
router.post('/verify-account', AuthController.verifyAccount);

export default router;