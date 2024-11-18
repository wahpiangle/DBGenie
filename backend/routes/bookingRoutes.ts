import express from 'express';
import { BookingController } from '../controller/bookingController';
import redirectLogin from '../middleware/redirectLogin';
const router = express.Router();

router.use(redirectLogin);
router.route('/')
    .post(BookingController.createBooking)
    .get(BookingController.getBookingsByUser);

router.route('/:id')
    .get(BookingController.getBooking)
    .delete(BookingController.deleteBooking)
    .patch(BookingController.editBooking);

router.route('/:propertyId')
    .post(BookingController.checkBookingsByProperty);


export default router;