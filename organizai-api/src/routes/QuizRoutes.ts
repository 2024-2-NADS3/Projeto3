import express from 'express';
import { QuizController } from '../controllers/QuizController';

const router = express.Router();
const quizController = new QuizController();

router.post('/' as any, quizController.inserirPerguntas as any)


export default router;