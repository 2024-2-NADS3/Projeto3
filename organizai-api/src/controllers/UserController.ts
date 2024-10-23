import { Request, Response } from 'express';
import { AppDataSource } from '../data-source';
import { User } from '../entities/User';
import { Categoria } from '../entities/Categoria';
import { validate } from 'class-validator';
import { QueryFailedError } from 'typeorm';
import bcrypt from 'bcryptjs';
import { Quiz } from '../entities/Quiz';

export class UserController {
  private userRepository = AppDataSource.getRepository(User);
  private catRepository = AppDataSource.getRepository(Categoria);
  private quizRepository = AppDataSource.getRepository(Quiz);
  

  createUser = async (req: Request, res: Response) => {
    try {
      const { categorias, ...userDto } = req.body;

      // Criar uma nova instância de User
      const newUser = new User();

      Object.assign(newUser, userDto);

      // Validar a entidade
      const errors = await validate(newUser);
      if (errors.length > 0) {

        const errorMessages = errors.map(error => 
          Object.values(error.constraints || {})
        ).flat();
        return res.status(400).json({ errors: errorMessages });
      }

      const saltRounds = 10;
      newUser.senha = await bcrypt.hash(newUser.senha, saltRounds);
      
      // Salvar o usuário
      const savedUser = await this.userRepository.save(newUser);

      // Criar e salvar as categorias associadas ao usuário
      if (categorias && Array.isArray(categorias)) {
        const categoriasEntities = categorias.map(cat => {
          const categoria = new Categoria();
          Object.assign(categoria, cat);
          categoria.usuario = savedUser;
          return categoria;
        });

        await this.catRepository.save(categoriasEntities);
      }

      // Buscar o usuário salvo com as categorias
      const userWithCategories = await this.userRepository.findOne({
        where: { UserId: savedUser.UserId },
        relations: ['categorias']
      });

      res.status(201).json(userWithCategories);
    } catch (error) {
      console.error(error);
      if (
        error instanceof QueryFailedError &&
        error.message.includes('duplicate key value violates unique constraint')
      ) {
        return res
          .status(409)
          .json({ message: 'Este email já está cadastrado' });
      }
      res.status(500).json({ message: 'Erro ao criar usuário' });
    }
  };

  findAllUsers = async (req: Request, res: Response) => {
    try {
      const users = await this.userRepository.find();
      if (!users || users.length === 0) {
        res.status(404).json({ message: 'Usuários não encontrados' });
      }
      res.json(users);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Erro ao buscar usuários' });
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
            relations: ['categorias', 'quiz', 'transacoes']
          });
          return res.json(userWithCategoriesAndQuiz);
        }
      } catch (error) {
        console.error(error);
         return res.status(500).json({ message: "Erro ao buscar usuário" });
      }
    }
    
  autenticacaoUser = async (req: Request, res: Response) => {
    try {
      const { email, senha } = req.body;

      if (!email || !senha) {
        return res
          .status(400)
          .json({ message: 'Email e senha são obrigatórios' });
      }

      // Busca o usuário pelo email
      const user = await this.userRepository.findOneBy({ email });

      if (!user) {
        return res.status(404).json({ message: 'Usuário não encontrado' });
      }

      // Verifica se a senha está correta
      const senhaValida = await bcrypt.compare(senha, user.senha);

      if (!senhaValida) {
        return res.status(401).json({ message: 'Senha incorreta' });
      }

      // Caso a autenticação seja bem-sucedida
      return res.sendStatus(200); // Sucesso, sem retorno de dados
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: 'Erro ao autenticar usuário' });
    }
  };

  resetPassword = async (req: Request, res: Response) => {
    try {
      const { email, newPassword } = req.body;

      if (!email || !newPassword) {
        return res
          .status(400)
          .json({ message: 'Email e nova senha são obrigatórios' });
      }

      const user = await this.userRepository.findOneBy({ email });

      if (!user) {
        return res.status(404).json({ message: 'Usuário não encontrado' });
      }

      const saltRounds = 10;
      user.senha = await bcrypt.hash(newPassword, saltRounds);

      await this.userRepository.save(user);

      return res.status(200).json({ message: 'Senha atualizada com sucesso' });
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Erro ao redefinir senha' });
    }
  };

  deleteUser = async (req: Request, res: Response) => {
    try {
      const { id } = req.params;
  
      if (!id) {
        return res.status(400).json({ message: "O Id não pode ser nulo." });
      }
      // Encontra o usuário
      const user = await this.userRepository.findOne({where: {
        UserId: Number(id)
      } });
  
      if (!user) {
        return res.status(404).json({ message: "Usuário não encontrado." });
      }

      const quiz = await this.quizRepository.findOne({ where: { UserId: Number(id) } });
    
      if (quiz) {
      await this.quizRepository.delete(quiz.QuizId);
      
    }
  
      await this.userRepository.delete(id);
      
      return res.status(200).json({ message: `Usuário de Id: ${id} excluído com sucesso, juntamente com o quiz.` });
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao excluir usuário" });
    }
  };
};

