import { Request, Response } from 'express';
import { AppDataSource } from '../data-source';
import { User } from '../entities/User';
import { Categoria } from '../entities/Categoria';
import { validate } from 'class-validator';
import { QueryFailedError } from 'typeorm';
import bcrypt from 'bcryptjs';

export class UserController {
  private userRepository = AppDataSource.getRepository(User);
  private catRepository = AppDataSource.getRepository(Categoria);
  


  createUser = async (req: Request, res: Response) => {
    try {
      const userDto = req.body;

      // Criar uma nova instância de User
      const newUser = new User();
      Object.assign(newUser, userDto); // copia o dto para a nova instancia

       // Validar a entidade
       const errors = await validate(newUser);
       if (errors.length > 0) {
         const errorMessages = errors.map(error => 
           Object.values(error.constraints || {})
         ).flat();
         return res.status(400).json({ errors: errorMessages });
       }
      // Buscar todas as categorias
      const categorias = await this.catRepository.find();

      // Associar as categorias ao novo usuário
      newUser.categorias = categorias;

      const saltRounds = 10; // Define o número de rounds de salt
      newUser.senha = await bcrypt.hash(newUser.senha, saltRounds);
      
      // Salvar o usuário com as categorias associadas
      const savedUser = await this.userRepository.save(newUser);

      // Buscar o usuário salvo com as categorias caso queria retonar o usuário com as categorias
      // const userWithCategories = await this.userRepository.findOne({
      //   where: { UserId: savedUser.UserId },
      //   relations: ['categorias']
      // });

      res.status(201).json(savedUser);
    } catch (error) {
      console.error(error);
      if (error instanceof QueryFailedError && error.message.includes('duplicate key value violates unique constraint')) {
        return res.status(409).json({ message: "Este email já está cadastrado" });
      }
      res.status(500).json({ message: "Erro ao criar usuário" });
    }
  };

  findAllUsers = async (req: Request, res: Response) => {
    try {
      const users = await this.userRepository.find();
      if (!users || users.length === 0) {
         res.status(404).json({ message: "Usuários não encontrados" });
      }
      res.json(users);
    } catch (error) {
      console.error(error);
       res.status(500).json({ message: "Erro ao buscar usuários" });
    }
  };

  findUserByEmail = async (req: Request, res: Response) => {
    try {
        const { email } = req.params; // Captura o email do path (req.params)
    
        if (!email) {
           return res.status(400).json({ message: "Email é obrigatório" });
        }
    
        const user = await this.userRepository.findOneBy({ email });
    
        if (!user) {
           return res.status(404).json({ message: "Usuário não encontrado" });
        } else {
          const userWithCategoriesAndQuiz = await this.userRepository.findOne({
            where: { UserId: user.UserId },
            relations: ['categorias', 'quiz']
          });
          return res.json(userWithCategoriesAndQuiz);
        }
      } catch (error) {
        console.error(error);
         return res.status(500).json({ message: "Erro ao buscar usuário" });
      }
  };

  autenticacaoUser = async (req: Request, res: Response) => {
    try {
      const { email, senha } = req.body;

      if (!email || !senha) {
        return res.status(400).json({ message: "Email e senha são obrigatórios" });
      }

      // Busca o usuário pelo email
      const user = await this.userRepository.findOneBy({ email });

      if (!user) {
        return res.status(404).json({ message: "Usuário não encontrado" });
      }

      // Verifica se a senha está correta
      const senhaValida = await bcrypt.compare(senha, user.senha);

      if (!senhaValida) {
        return res.status(401).json({ message: "Senha incorreta" });
      }

      // Caso a autenticação seja bem-sucedida
      return res.sendStatus(200); // Sucesso, sem retorno de dados
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao autenticar usuário" });
    }
  };
}