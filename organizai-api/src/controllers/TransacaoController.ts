import { QueryFailedError } from "typeorm";
import { AppDataSource } from "../data-source";
import { Request, Response } from 'express';
import { Transacao } from "../entities/Transacao";

export class TransacaoController {
    private transRepository = AppDataSource.getRepository(Transacao);

    inserirTransacao = async (req: Request, res: Response) =>{
        try {

            const tranDto = req.body;

            const tranSaved = await this.transRepository.save(tranDto);

            res.status(201).json(tranSaved);
        }catch (error) {
            // Logando a mensagem de erro no caso de erro 500
            const errorMessage = error instanceof Error ? error.message : "Erro desconhecido";
            console.error(`Erro ao insirir transacao: ${errorMessage}`);

            res.status(500).json({ message: "Erro ao insirir transacao", error: errorMessage });
        }
    };
}