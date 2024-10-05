import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { AuthController } from '../controller/authController';
const router = express.Router();

router.get('/', redirectLogin, (req, res) => {
    res.send(`Welcome ${req.session.user}`);
});

router.post('/register', AuthController.register);
router.post('/login', AuthController.login);
router.get('/logout', AuthController.logout);

export default router;