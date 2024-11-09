import express from 'express';
import { PropertyController } from '../controller/propertyController';
import redirectLogin from '../middleware/redirectLogin';
import checkManager from '../middleware/checkManager';
const router = express.Router();

router.use(redirectLogin);

router.route('/')
    .get(PropertyController.getPropertiesByUser)
    .post(checkManager, PropertyController.createProperty);

router.route('/:id')
    .get(PropertyController.getProperty)
    .delete(checkManager, PropertyController.removeProperty)
    .patch(checkManager, PropertyController.updateProperty);
export default router;