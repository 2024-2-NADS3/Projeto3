import express from 'express';
import { UserController } from '../controllers/UserController';

const router = express.Router();
const userController = new UserController();

// a utilizacao de 'as any' é bem zuada, nao sei como resolver de outra forma por enquanto
router.get('/' as any, userController.findAllUsers as any)
router.get('/:email' as any, userController.findUserByEmail as any)
router.post('/' as any, userController.createUser as any)
router.post('/isUsuario' as any, userController.autenticacaoUser as any)

export default router;