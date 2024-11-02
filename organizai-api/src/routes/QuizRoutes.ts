import express from 'express';
import { QuizController } from '../controllers/QuizController';

const router = express.Router();
const quizController = new QuizController();

router.post('/' as any, quizController.inserirPerguntas as any)
router.get('/:userId', quizController.getQuizByUserId as any );
router.get('/:userId/elegibilidade', quizController.verificarElegibilidade as any);


export default router;