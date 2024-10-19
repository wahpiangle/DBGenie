import express from 'express';
import redirectLogin from '../middleware/redirectLogin';
import { MaintenanceRequestController } from '../controller/maintenanceRequestController';
const router = express.Router();
router.use(redirectLogin);
router.route('/')
    .post(MaintenanceRequestController.createMaintenanceRequest);


router.route('/:propertyId')
    .get(MaintenanceRequestController.getMaintenanceRequestByProperty);