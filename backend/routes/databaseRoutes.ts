import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { DatabaseController } from '../controller/databaseController';
const router = express.Router();
router.use(redirectLogin);

router.route('/')
    .get(DatabaseController.getDatabaseTables)

export default router;