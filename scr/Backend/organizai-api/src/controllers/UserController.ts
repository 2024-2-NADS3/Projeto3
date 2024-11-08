import { Request, Response } from 'express';
import { AppDataSource } from '../data-source';
import { User } from '../entities/User';
import { Categoria } from '../entities/Categoria';
import { validate } from 'class-validator';
import { QueryFailedError } from 'typeorm';
import bcrypt from 'bcryptjs';
import { Quiz } from '../entities/Quiz';
import { Transacao } from '../entities/Transacao';
import { redisService } from '../redisconfig';

export class UserController {
  private userRepository = AppDataSource.getRepository(User);
  private catRepository = AppDataSource.getRepository(Categoria);
  private quizRepository = AppDataSource.getRepository(Quiz);
  private tranRepository = AppDataSource.getRepository(Transacao);
  

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

        // Tenta buscar do cache primeiro
        const cachedUser = await redisService.get(`user:email:${email}`);
        if (cachedUser) {
          console.info("Busca por cache " + cachedUser)
          return res.json(cachedUser);
        }
    
        const user = await this.userRepository.findOneBy({ email });
    
        if (!user) {
           return res.status(404).json({ message: "Usuário não encontrado" });
        } else {
          const userWithCategoriesAndQuiz = await this.userRepository.findOne({
            where: { UserId: user.UserId },
            relations: ['categorias', 'quiz', 'transacoes']
          });

           // Salva no cache por 1 hora (3600 segundos)
          await redisService.set(`user:email:${email}`, userWithCategoriesAndQuiz, 3600);
      
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
      const user = await this.userRepository.findOne({
        where: { UserId: Number(id) },
        relations: ["categorias","quiz", "transacoes"], // Certifique-se de que isso está correto
      });
  
      if (!user) {
        return res.status(404).json({ message: "Usuário não encontrado." });
      }

      if(user.quiz !== null){
        await this.quizRepository.delete(user.quiz.UserId);
      }
      
      if(user.transacoes.length !== 0){
        await this.tranRepository
       .createQueryBuilder()
       .delete()
       .where("usuario.UserId = :id", { id })
       .execute();
       console.log("Transações deletadas ");
     }
    
       if(user.categorias.length !== 0){
         await this.catRepository
         .createQueryBuilder()
         .delete()
         .where("usuario.UserId = :id", { id })
         .execute();
         console.log("Categorias deletadas ");
       }
  
      await this.userRepository.delete(id);
      
      return res.status(200).json({ message: `Usuário de Id: ${id} excluído com sucesso, juntamente com o quiz.` });
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao excluir usuário" });
    }
  };

  buscaTransacoesUserByParams = async (req: Request, res: Response) => {
    try {
        const { userId, tipoCategoria, mes, ano } = req.params;

        // Verifica se todos os parâmetros obrigatórios foram fornecidos
        if (!userId || !mes || !ano) {
            return res.status(400).json({ message: 'Todos os parâmetros são obrigatórios.' });
        }
        // Tenta buscar do cache primeiro
      const cacheKey = `user:${userId}:tipo-${tipoCategoria}:mes-${mes}:ano-${ano}`;
      const cachedResult = await redisService.get(cacheKey);
      if (cachedResult) {
        console.info("Busca por cache " + cacheKey)
        return res.status(200).json(cachedResult);
      }

        // Busca as categorias com base no tipoCategoria
        const categorias = await this.catRepository.find({
            where: {
                usuario: { UserId: Number(userId) },
                ...(tipoCategoria !== '0' ? { tipo: Number(tipoCategoria) } : {}),
            },
        });

        // Se não encontrar categorias, retornar um status 404
        if (categorias.length === 0) {
            return res.status(404).json({ message: 'Nenhuma categoria encontrada.' });
        }

        // Buscar as transações vinculadas às categorias usando o 'Id'
        const transacoes = await AppDataSource.createQueryBuilder()
            .select('t')
            .from(Transacao, 't')
            .where('t.CategoriaId IN (:...categoriaIds)', { categoriaIds: categorias.map(cat => cat.Id) })
            .andWhere('EXTRACT(MONTH FROM t.data) = :mes', { mes })
            .andWhere('EXTRACT(YEAR FROM t.data) = :ano', { ano })
            .getMany();

        // Estruturar o resultado
        const resultado = categorias
            .map(categoria => {
                // Filtra as transações que correspondem a esta categoria
                const transacoesRelacionadas = transacoes.filter(t => t.CategoriaId === categoria.Id); // Filtra usando apenas o ID da categoria

                // Retorna apenas as categorias que têm transações relacionadas
                return {
                    categoria: {
                        Id: categoria.Id,  // Inclui o Id da categoria
                        CategoriaId: categoria.CategoriaId, // Inclui o CategoriaId
                        nomeCat: categoria.nomeCat,
                        tipo: categoria.tipo,
                        total: categoria.total,
                    },
                    transacoes: transacoesRelacionadas.map(({ categoria, ...transacao }) => transacao), // Remove a categoria da transação
                };
            })
            .filter(item => item.transacoes.length > 0); // Filtra para manter apenas categorias com transações

          // Salva o resultado no cache por 1 hora (3600 segundos)
          await redisService.set(cacheKey, resultado, 3600);
        // Retorna o resultado com as categorias e suas transações
        return res.status(200).json(resultado);
    } catch (error) {
        console.error(error);
        return res.status(500).json({ message: 'Erro ao buscar categorias e transações.' });
    }
};

};

