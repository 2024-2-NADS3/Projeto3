import request from 'supertest';
import { app } from '../../app';
import { UserController } from '../../controllers/UserController';
import { User } from '../../entities/User';


const mockUserRepository = {
  find: jest.fn(),
};


jest.mock('../../src/data-source', () => ({
  AppDataSource: {
    getRepository: jest.fn(() => mockUserRepository),
  },
}));

describe('UserController - findAllUsers', () => {
  let userController: UserController;

  beforeAll(() => {
    userController = new UserController();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('deve retornar status 200 e uma lista de usuários quando houver usuários', async () => {
    const mockUsers: Partial<User>[] = [
      { UserId: 1, nome: 'Usuário Teste' },
      { UserId: 2, nome: 'Outro Usuário' },
    ];

    (mockUserRepository.find as jest.Mock).mockResolvedValue(mockUsers as User[]);

    const response = await request(app).get('/users');

    expect(response.status).toBe(200);
    expect(response.body).toEqual(mockUsers);
  });

  it('deve retornar status 404 quando não houver usuários', async () => {
    (mockUserRepository.find as jest.Mock).mockResolvedValue([]);

    const response = await request(app).get('/users');

    expect(response.status).toBe(404);
    expect(response.body).toEqual({ message: 'Usuários não encontrados' });
  });

  it('deve retornar status 500 quando ocorre um erro no servidor', async () => {
    (mockUserRepository.find as jest.Mock).mockRejectedValue(new Error('Erro ao buscar usuários'));

    const response = await request(app).get('/users');

    expect(response.status).toBe(500);
    expect(response.body).toEqual({ message: 'Erro ao buscar usuários' });
  });
});
