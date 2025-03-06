import "reflect-metadata"
import express from 'express';
import cors from 'cors';
import { AppDataSource } from "./data-source"
import userRoutes from './routes/UserRoutes';
import quizRoutes from './routes/QuizRoutes';
import assistentRoutes from './routes/AssistentRoutes'
import transacaoRoutes from './routes/TransacaoRoutes';
import catRoutes from './routes/CategoriaRoutes';
import crypto from 'crypto';


export const app = express();
const port = process.env.PORT || 8080;
// Configuração básica do CORS
app.use(cors());

app.use(express.json());

// Função para gerar chave AES de 256 bits (32 bytes) em formato hexadecimal
function generateAESKey() {
    return crypto.randomBytes(32).toString('hex');
}

// Rota para gerar e retornar a chave AES
app.get('/generate-key', (req, res) => {
    try {
        
        const aesKey = generateAESKey();
        res.json({ key: aesKey });
    } catch (error) {
        res.status(500).json({ message: 'Erro ao gerar chave AES', error });
    }
});


AppDataSource.initialize().then(async () => {
    console.log("Data Source foi inicializado!")

    app.use('/users', userRoutes);
    
    app.use('/quiz', quizRoutes);

    app.use('/transacao', transacaoRoutes);

    app.use('/categoria', catRoutes);

    app.use('/assistent', assistentRoutes)

    
    app.listen(port, () => console.log(`Server running on port ${port}`));

}).catch(error => console.log(error))