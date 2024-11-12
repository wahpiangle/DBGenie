import express from 'express';
import { PropertyController } from '../controller/propertyController';
import redirectLogin from '../middleware/redirectLogin';
import checkManager from '../middleware/checkManager';
import multer from 'multer';
const router = express.Router();
const upload = multer({ dest: 'uploads/' })
router.use(redirectLogin);

router.route('/')
    .get(PropertyController.getPropertiesByUser)
    .post(checkManager,
        upload.array('files', 10),
        PropertyController.createProperty);

router.route('/:id')
    .get(PropertyController.getProperty)
    .delete(checkManager, PropertyController.removeProperty)
    .patch(checkManager, PropertyController.updateProperty);
export default router;