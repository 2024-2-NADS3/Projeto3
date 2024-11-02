import express from 'express';
import { CategoriaController } from '../controllers/CategoriaController';


const router = express.Router();
const catController = new CategoriaController();

// a utilizacao de 'as any' é bem zuada, nao sei como resolver de outra forma por enquanto
router.put('/' as any, catController.updateCategoriasUser as any)


export default router;