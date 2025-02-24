import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { DatabaseController } from '../controller/databaseController';
const router = express.Router();
router.use(redirectLogin);

router.route('/')
    .get(DatabaseController.getDatabaseTables)

router.route('/get-table-info')
    .get(DatabaseController.getTableInfo)

router.route('/delete-row')
    .delete(DatabaseController.deleteRow)
export default router;