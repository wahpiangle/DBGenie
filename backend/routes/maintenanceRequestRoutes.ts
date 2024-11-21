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

router.route("/resolve/:id")
    .get(MaintenanceRequestController.resolveMaintenanceRequest);

router.route('/:propertyId')
    .get(MaintenanceRequestController.getMaintenanceRequestByProperty);

router.route("/:maintenanceRequestId/update")
    .post(
        upload.single('file'),
        MaintenanceRequestController.createMaintenanceRequestUpdate
    )

export default router;