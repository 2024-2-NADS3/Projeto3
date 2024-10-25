import { Between, QueryFailedError } from "typeorm";
import { AppDataSource } from "../data-source";
import { Request, Response } from 'express';
import { Transacao } from "../entities/Transacao";
import { User } from "../entities/User";
import { Categoria } from "../entities/Categoria";

export class TransacaoController {
    private transRepository = AppDataSource.getRepository(Transacao);
    private userRepository = AppDataSource.getRepository(User);
    private catRepository = AppDataSource.getRepository(Categoria);
    
    inserirTransacao = async (req: Request, res: Response) =>{
        try {

            const tranDto = req.body;

            const tranSaved = await this.transRepository.save(tranDto);

            const cat = await this.catRepository.findOne({ 
                where: { 
                    CategoriaId: tranSaved.CategoriaId,
                    usuario: tranSaved.UserId
                 } })

                 res.status(201).json({ 
                    transacao: tranSaved,
                    categoria: cat 
                });
        }catch (error) {
            // Logando a mensagem de erro no caso de erro 500
            const errorMessage = error instanceof Error ? error.message : "Erro desconhecido";
            console.error(`Erro ao insirir transacao: ${errorMessage}`);

            res.status(500).json({ message: "Erro ao insirir transacao", error: errorMessage });
        }
    };

    buscarTransacoesByParams = async (req: Request, res: Response) => {
        const { email, mes, ano } = req.query;
    
        if (!email || !mes || !ano) {
            return res.status(400).json({ message: "Parâmetros inválidos" });
        }
    
        try {
            // Buscando o usuário pelo email
            const usuario = await this.userRepository.findOne({ where: { email: String(email) } });
    
            if (!usuario) {
                return res.status(404).json({ message: "Usuário não encontrado" });
            }
    
            // Construindo as datas como objetos Date
            const startDate = new Date(Number(ano), Number(mes) - 1, 1);  // Mês começa de 0 em JavaScript
            const endDate = new Date(Number(ano), Number(mes), 0);  // O último dia do mês
    
            // Fazendo a consulta das transações pelo ID do usuário e data
            const transacoes = await this.transRepository.find({
                where: {
                    UsuarioId: usuario.UserId,  // Usando o UsuarioId encontrado
                    data: Between(startDate, endDate),  // Usando objetos Date no Between
                },
                relations: ["categoria"], // Inclui as categorias relacionadas
                order: {
                    data: "DESC"
                }
            });
    
            res.status(200).json(transacoes);
        } catch (error) {
            console.error("Erro ao buscar transações:", error);
            res.status(500).json({ message: "Erro ao buscar transações", error });
        }
    };
    
}

