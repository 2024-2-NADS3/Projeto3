import Redis from 'ioredis';
import dotenv from 'dotenv';

dotenv.config();

const redisClient = new Redis({
  host: process.env.REDIS_HOST || 'seu-nome-do-cache.redis.cache.windows.net',
  port: parseInt(process.env.REDIS_PORT || '6380'),
  password: process.env.REDIS_PASSWORD || 'sua-chave-de-acesso',
  tls: {
    servername: process.env.REDIS_HOST || 'seu-nome-do-cache.redis.cache.windows.net'
  }
});

// Tratamento de eventos de conexão
redisClient.on('connect', () => {
  console.log('Conectado ao Redis');
});

redisClient.on('error', (error) => {
  console.error('Erro na conexão com Redis:', error);
});

// Classe helper para operações com Redis
export class RedisService {
  private static instance: RedisService;
  private client: Redis;

  private constructor() {
    this.client = redisClient;
  }

  public static getInstance(): RedisService {
    if (!RedisService.instance) {
      RedisService.instance = new RedisService();
    }
    return RedisService.instance;
  }

  // Método para salvar dados no cache
  async set(key: string, value: any, ttl?: number): Promise<void> {
    try {
      const stringValue = JSON.stringify(value);
      if (ttl) {
        await this.client.setex(key, ttl, stringValue);
      } else {
        await this.client.set(key, stringValue);
      }
    } catch (error) {
      console.error('Erro ao salvar no cache:', error);
      throw error;
    }
  }

  // Método para recuperar dados do cache
  async get(key: string): Promise<any> {
    try {
      const value = await this.client.get(key);
      return value ? JSON.parse(value) : null;
    } catch (error) {
      console.error('Erro ao recuperar do cache:', error);
      throw error;
    }
  }

  // Método para deletar dados do cache
  async delete(key: string): Promise<void> {
    try {
      await this.client.del(key);
    } catch (error) {
      console.error('Erro ao deletar do cache:', error);
      throw error;
    }
  }
}

// Exporta a instância única do RedisService
export const redisService = RedisService.getInstance();