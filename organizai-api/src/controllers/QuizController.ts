import { QueryFailedError } from "typeorm";
import { AppDataSource } from "../data-source";
import { Quiz } from "../entities/Quiz";
import { Request, Response } from 'express';

export class QuizController {
    private quizRepository = AppDataSource.getRepository(Quiz);

    // Método para inserir ou atualizar perguntas do quiz
    inserirPerguntas = async (req: Request, res: Response) => {
        try {
            const quizDto = req.body;

            // Verifica se já existe um quiz para o UserId informado
            const existingQuiz = await this.quizRepository.findOneBy({ UserId: quizDto.UserId });
            
            if (existingQuiz) {
                if (!quizDto.isAnswered) {
                    return res.status(204).json({ message: "Um quiz já existe para este usuário e não foi respondido." });
                } else {
                    Object.assign(existingQuiz, quizDto);
                    const updatedQuiz = await this.quizRepository.save(existingQuiz);
                    return res.status(200).json({ message: "Quiz atualizado com sucesso", quiz: updatedQuiz });
                }
            }

            // Cria um novo quiz caso não exista um para o UserId
            const newQuiz = new Quiz();
            Object.assign(newQuiz, quizDto);
            const savedQuiz = await this.quizRepository.save(newQuiz);
            res.status(201).json(savedQuiz);

        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : "Erro desconhecido";
            console.error(`Erro ao criar quiz do usuário: ${errorMessage}`);
            res.status(500).json({ message: "Erro ao criar quiz do usuário", error: errorMessage });
        }
    };

    // Método para obter o quiz de um usuário por ID
    getQuizByUserId = async (req: Request, res: Response) => {
        try {
            const { userId } = req.params;
            const quiz = await this.quizRepository.findOneBy({ UserId: Number(userId) });

            if (!quiz) {
                return res.status(404).json({ message: "Quiz não encontrado para o usuário especificado" });
            }

            res.status(200).json(quiz);

        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : "Erro desconhecido";
            console.error("Erro ao buscar o quiz:", errorMessage);
            res.status(500).json({ message: "Erro ao buscar o quiz", error: errorMessage });
        }
    };

    // Método para verificar a elegibilidade com base nas respostas do quiz
    verificarElegibilidade = async (req: Request, res: Response) => {
        try {
            const userId = parseInt(req.params.userId, 10);
            if (isNaN(userId)) {
                return res.status(400).json({ message: "O ID do usuário deve ser um número válido." });
            }

            const quiz = await this.quizRepository.findOneBy({ UserId: userId });

            if (!quiz) {
                return res.status(404).json({ message: "Quiz não encontrado para o usuário especificado" });
            }

            // Definir critérios de elegibilidade baseados nos dados do quiz
            const elegibilidade = {
                cadastroUnico: quiz.isCadUni ?? false,
                bolsaFamilia: quiz.rendaMensal ? quiz.rendaMensal <= 218 : false,
                beneficioIdoso: quiz.isOlder ?? false,
                fomentoRural: quiz.rendaMensal && quiz.isRural ? quiz.rendaMensal <= 400 : false,
            };

            res.status(200).json({ elegivel: elegibilidade });

        } catch (error) {
            console.error("Erro ao verificar elegibilidade:", error);
            const errorMessage = error instanceof Error && error.message ? error.message : "Erro desconhecido";
            res.status(500).json({ message: "Erro ao verificar elegibilidade", error: errorMessage });
        }
    };
}
