import "reflect-metadata"
import express from 'express';
import cors from 'cors';
import { AppDataSource } from "./data-source"
import userRoutes from './routes/UserRoutes';
import quizRoutes from './routes/QuizRoutes';
import transacaoRoutes from './routes/TransacaoRoutes';
import catRoutes from './routes/CategoriaRoutes';


export const app = express();
const port = process.env.PORT || 8080;
// Configuração básica do CORS
app.use(cors());

app.use(express.json());

AppDataSource.initialize().then(async () => {
    console.log("Data Source foi inicializado!")

    app.use('/users', userRoutes);
    
    app.use('/quiz', quizRoutes);

    app.use('/transacao', transacaoRoutes);

    app.use('/categoria', catRoutes);

    
    app.listen(port, () => console.log(`Server running on port ${port}`));

}).catch(error => console.log(error))