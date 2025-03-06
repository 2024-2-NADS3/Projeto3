import { Request, Response } from 'express';
import ModelClient from "@azure-rest/ai-inference";
import { AzureKeyCredential } from "@azure/core-auth";

interface Message {
    role: string;
    content: string;
}

export class AssistentController {
    private client: any; 

    constructor() {
      // @ts-ignore - Ignorando temporariamente o erro de tipagem do construtor
        this.client = new ModelClient(
            process.env.AZURE_INFERENCE_SDK_ENDPOINT || "",
            new AzureKeyCredential(process.env.AZURE_INFERENCE_SDK_KEY || "")
        );
    }

    public execute = async (req: Request, res: Response): Promise<void> => {
        try {
            
          // Verifica se existe uma mensagem no body
            if (!req.body.message) {
              res.status(400).json({
                  success: false,
                  error: 'Message is required in request body'
              });
              return;
          }

          const messages: Message[] = [
              { role: "system", content: "Você é um assistente que da respostas sucintas em português sem emojis." },
              { role: "user", content: req.body.message }
          ];

            const response = await this.client.path("chat/completions").post({
                body: {
                    messages: messages,
                    max_tokens: 1000,
                    model: process.env.DEPLOYMENT_NAME || "DeepSeek-R1",
                }
            });
            // Pegando apenas a resposta final
            const content = response.body?.choices?.[0]?.message?.content || "";

            // Removendo os pensamentos dentro da tag <think>
            const cleanContent = content.replace(/<think>[\s\S]*?<\/think>\n*/g, "").trim();

           console.info(content)
           
            res.status(200).json({
                success: true,
                data: cleanContent
            });

        } catch (error) {
            console.error('Erro ao processar a requisição:', error);
            res.status(500).json({
                success: false,
                error: 'Erro ao processar a requisição'
            });
        }
    }
}