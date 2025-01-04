import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { ChatController } from '../controller/chatController';
const router = express.Router();
router.use(redirectLogin);

router.route('/')
    .post(ChatController.createChat)

export default router;