import { applicationDefault, initializeApp } from 'firebase-admin/app';

const firebaseApp = initializeApp({
    credential: applicationDefault(),
});

export { firebaseApp };