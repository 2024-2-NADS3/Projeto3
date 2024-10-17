import { QueryFailedError } from "typeorm";
import { AppDataSource } from "../data-source";
import { Quiz } from "../entities/Quiz";
import { Request, Response } from 'express';

export class QuizController {
    private quizRepository = AppDataSource.getRepository(Quiz);

    inserirPerguntas = async (req: Request, res: Response) =>{
        try {

            const quizDto = req.body;

            // Verifica se já existe um quiz para esse UserId
            const existingQuiz = await this.quizRepository.findOneBy({ UserId: quizDto.UserId });
            
            if (existingQuiz) {
                // Se já houver um quiz, retorna 200 com uma mensagem apropriada
                return res.status(200).json({ message: "Um quiz já existe para este usuário"});
            }

            const newQuiz = new Quiz();
            Object.assign(newQuiz, quizDto);

            const savedQuiz = await this.quizRepository.save(newQuiz);

            res.status(201).json(savedQuiz);
        }catch (error) {
            // Logando a mensagem de erro no caso de erro 500
            const errorMessage = error instanceof Error ? error.message : "Erro desconhecido";
            console.error(`Erro ao criar quiz do usuário: ${errorMessage}`);

            res.status(500).json({ message: "Erro ao criar quiz do usuário", error: errorMessage });
        }
    };
}