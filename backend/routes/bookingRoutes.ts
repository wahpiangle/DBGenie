import express from 'express';
import { BookingController } from '../controller/bookingController';
import redirectLogin from '../middleware/redirectLogin';
import checkManager from '../middleware/checkManager';
const router = express.Router();

router.use(redirectLogin);
router.route('/')
    // .get(BookingController.getBookings)
    .post(BookingController.createBooking);

router.route('/:id')
// .get(BookingController.getBooking)
// .delete(checkManager, BookingController.removeBooking)
// .patch(checkManager, BookingController.updateBooking);

export default router;