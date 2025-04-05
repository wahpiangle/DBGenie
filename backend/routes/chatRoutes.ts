import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { ChatController } from '../controller/chatController';
const router = express.Router();
router.use(redirectLogin);

router.route('/')
    .post(ChatController.createChat)
    .get(ChatController.getChatHistory);

router.get('/clear', (req, res) => ChatController.clearChatHistory(req, res));

export default router;