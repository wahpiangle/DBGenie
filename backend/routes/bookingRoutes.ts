import express from 'express';
import { BookingController } from '../controller/bookingController';
import redirectLogin from '../middleware/redirectLogin';
const router = express.Router();

router.use(redirectLogin);
router.route('/')
    .post(BookingController.createBooking);

router.route('/:id')
    .get(BookingController.getBooking)
    .delete(BookingController.deleteBooking)
    .put(BookingController.editBooking);

router.route('/:propertyId')
    .post(BookingController.checkBookingsByProperty);


export default router;