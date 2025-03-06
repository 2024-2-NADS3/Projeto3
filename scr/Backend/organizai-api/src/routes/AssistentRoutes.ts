import express from 'express';
import { AssistentController } from '../controllers/AssistentController';

const router = express.Router();
const assistentController = new AssistentController();

router.post('/', (req, res) => assistentController.execute(req, res));

export default router;