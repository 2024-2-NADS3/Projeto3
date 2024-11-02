import { DataSourceOptions } from "typeorm";
import dotenv from 'dotenv';

dotenv.config();
const isProd = process.env.NODE_ENV === 'prod';
const config: DataSourceOptions = {
  type: "postgres",
  host: process.env.DB_HOST,
  port: parseInt(process.env.DB_PORT || "5432"),
  username: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
  entities: [
    isProd ? "dist/entities/**/*.js" : "src/entities/**/*.ts"
  ],
  migrations: [
    isProd ? "dist/migrations/**/*.js" : "src/migrations/**/*.ts"
  ],
  subscribers: [
    isProd ? "dist/subscribers/**/*.js" : "src/subscribers/**/*.ts"
  ],
  synchronize: false,
  logging: true,
  ssl: {
    rejectUnauthorized: false
  }
};

export default config;