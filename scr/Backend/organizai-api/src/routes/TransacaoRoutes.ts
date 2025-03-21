import express from 'express';
import { TransacaoController } from '../controllers/TransacaoController';


const router = express.Router();
const tranController = new TransacaoController();

// a utilizacao de 'as any' é bem zuada, nao sei como resolver de outra forma por enquanto
router.post('/' as any, tranController.inserirTransacao as any)
router.get('/' as any, tranController.buscarTransacoesByParams as any)
router.delete('/:id' as any, tranController.excluirTransacao as any)

export default router;