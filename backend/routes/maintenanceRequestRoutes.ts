import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { MaintenanceRequestController } from '../controller/maintenanceRequestController';
import multer from 'multer';

const router = express.Router();
const upload = multer({ dest: 'uploads/' })
router.use(redirectLogin);
router.route('/')
    .post(
        upload.array('files', 10),
        MaintenanceRequestController.createMaintenanceRequest)
    .get(MaintenanceRequestController.getMaintenanceRequestByUser);

router.route('/:id')
    .get(MaintenanceRequestController.getMaintenanceRequest)

router.route('/:propertyId')
    .get(MaintenanceRequestController.getMaintenanceRequestByProperty);

export default router;