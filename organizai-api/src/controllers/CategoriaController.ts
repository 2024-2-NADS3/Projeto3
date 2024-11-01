import { AppDataSource } from "../data-source";
import { Request, Response } from "express";
import { User } from "../entities/User";
import { Categoria } from "../entities/Categoria";

export class CategoriaController {
  private userRepository = AppDataSource.getRepository(User);
  private catRepository = AppDataSource.getRepository(Categoria);

  updateCategoriasUser = async (req: Request, res: Response) => {
    try {
      const { UserId, categorias } = req.body; // 'categorias' é a lista de categorias do dispositivo
      
      console.log("userId extraído do req.body:", UserId);
      // Buscar o usuário no banco de dados
      const user = await this.userRepository.findOne({
        where: { UserId: UserId },
        relations: ["categorias"], // Certifique-se de que isso está correto
      });

      if (!user) {
        return res.status(404).json({ message: "Usuário não encontrado." });
      }

      
      await this.catRepository
      .createQueryBuilder()
      .delete()
      .where("usuario.UserId = :UserId", { UserId })
      .execute();
   
        
      console.log("Categorias deletadas " + UserId);

     // Criar novas categorias a partir das categorias enviadas pelo dispositivo
      const novasCategorias = categorias.map(
        (categoriaDoDispositivo: {
          Id: number;
          CategoriaId: number;
          nomeCat: string;
          tipo: number;
          total: number;
        }) => {
          return {
            CategoriaId: categoriaDoDispositivo.CategoriaId, // ID da categoria
            nomeCat: categoriaDoDispositivo.nomeCat, // Nome da categoria
            tipo: categoriaDoDispositivo.tipo, // Tipo da categoria (1 ou 2)
            total: categoriaDoDispositivo.total, // Total da categoria
            usuario: user, // Vincula a nova categoria ao usuário
          };
        }
      );

      // Salvar as novas categorias no banco de dados
      await this.catRepository.save(novasCategorias);

      return res
        .status(200)
        .json({ message: "Categorias atualizadas com sucesso!" });
    } catch (error) {
      const errorMessage =
        error instanceof Error ? error.message : "Erro desconhecido";
      console.error(`Erro ao atualizar categorias: ${errorMessage}`);
      return res
        .status(500)
        .json({ message: "Erro ao atualizar categorias", error: errorMessage });
    }
  };
}
