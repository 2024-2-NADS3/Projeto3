import { Request, Response } from 'express';
import ModelClient from "@azure-rest/ai-inference";
import { AzureKeyCredential } from "@azure/core-auth";
import { AzureMapsCLient } from "../client/AzureMapsClient";
import { SYSTEM_MESSAGES } from '../config';

interface Message {
    role: string;
    content: string | undefined;
}

export class AssistentController {
    private client: any;
    private azureMaps: AzureMapsCLient;

    constructor() {
        // @ts-ignore - Ignorando temporariamente o erro de tipagem do construtor
        this.client = new ModelClient(
            process.env.AZURE_INFERENCE_SDK_ENDPOINT || "",
            new AzureKeyCredential(process.env.AZURE_INFERENCE_SDK_KEY || "")
        );
        this.azureMaps = new AzureMapsCLient();
    }

    public execute = async (req: Request, res: Response): Promise<void> => {
        try {
            const { mensagem, origem, destino, historico }: { mensagem: string; origem: string; destino: string; historico: Message[] }  = req.body;
            let respostaIA = '';

            if (origem && destino) {
                respostaIA = await this.gerarChamadaDeEstimativaDePreco(origem, destino, []);
            } else if (mensagem) {
                respostaIA = await this.enviarParaIA(mensagem, SYSTEM_MESSAGES.FINANCAS, historico);
            } else {
                res.status(400).json({
                    success: false,
                    error: 'Uma mensagem ou um par de coordenadas (origem e destino) são necessários para fazer a requisição.'
                });
                return;
            }

            res.status(200).json({
                success: true,
                data: respostaIA
            });

        } catch (error) {
            console.error('Erro ao processar a requisição:', error);
            res.status(500).json({
                success: false,
                error: 'Erro ao processar a requisição'
            });
        }
    };

    private async gerarChamadaDeEstimativaDePreco(origem: string, destino: string, historico:Message[]): Promise<string> {
        try {
            const {distancia, duracao} = await this.azureMaps.calcularDistancia(origem, destino);
            const mensagem = `Minha localização atual é ${origem} e quero ir até ${destino}. Esse percurso tem uma distância de ${distancia.toFixed(2)} km e levará cerca de ${duracao.toFixed(2)} minutos considerando o tráfego atual. Me dê uma estimativa de preço entre Uber e 99 e me diga qual vale mais a pena.`;

            return await this.enviarParaIA(mensagem, SYSTEM_MESSAGES.CORRIDAS, historico);

        } catch (error) {
            console.error('Erro ao calcular estimativa de preço:', error);
            throw error;
        }
    }

    private async enviarParaIA(mensagem: string, systemMessage: string, historico:Message[]): Promise<string> {
        try {
            const messages: Message[] = [
                { role: "system", content: systemMessage },
                ...historico, 
                { role: "user", content: mensagem }
            ];
       
            console.info(messages)

            const response = await this.client.path("chat/completions").post({
                body: {
                    messages: messages,
                    max_tokens: 1000,
                    model: process.env.DEPLOYMENT_NAME || "DeepSeek-R1",
                    store:true
                }
            });

            // Pegando apenas a resposta final
            const content = response.body?.choices?.[0]?.message?.content || "";

            console.info(content)
            // Removendo os pensamentos dentro da tag <think>
            return content.replace(/<think>[\s\S]*?<\/think>\n*/g, "").replace(/\n/g, "<br>").trim();

        } catch (error) {
            console.error('Erro ao enviar mensagem para IA:', error);
            throw error;
        }
    }
}
