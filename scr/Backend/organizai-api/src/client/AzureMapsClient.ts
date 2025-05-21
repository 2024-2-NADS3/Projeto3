import axios from "axios";

interface ResponseMap {
    distancia: number;
    duracao: number;
    origemLat: number;
    origemLong: number;
    destLat: number;
    destLong: number;
}

export class AzureMapsCLient {
    private readonly subscriptionKey: string;
    private readonly baseUrl: string;

    constructor() {
        this.subscriptionKey = process.env.AZURE_MAPS_KEY || "";
        this.baseUrl = 'https://atlas.microsoft.com';

        if (!this.subscriptionKey) {
            throw new Error("A chave da API do Azure Maps não foi encontrada no .env.");
        }
    }

    /**
     * Calcula a distância entre dois endereços em quilômetros e retorna dados detalhados.
     * @param origem Endereço de origem
     * @param destino Endereço de destino
     */
    async calcularDistancia(origem: string, destino: string): Promise<ResponseMap> {
        try {
            // Obter coordenadas dos endereços
            const origemCoords = await this.geocodificarEndereco(origem);
            const destinoCoords = await this.geocodificarEndereco(destino);

            if (!origemCoords || !destinoCoords) {
                throw new Error("Não foi possível obter as coordenadas dos endereços.");
            }

            // Construir a URL da API de Roteamento do Azure Maps
            const url = `${this.baseUrl}/route/directions/json?api-version=1.0&subscription-key=${this.subscriptionKey}&query=${origemCoords.lat},${origemCoords.lon}:${destinoCoords.lat},${destinoCoords.lon}`;

            // Fazer a requisição
            const response = await axios.get(url);

            // Pegar a distância em metros e duração em segundos
            const distanceInMeters = response.data.routes[0].summary.lengthInMeters;
            const duracaoInSeconds = response.data.routes[0].summary.travelTimeInSeconds;

            const distancia = Math.round((distanceInMeters / 1000) * 100) / 100; // km, 2 casas decimais
            const duracao = Math.round((duracaoInSeconds / 60) * 100) / 100;     // minutos, 2 casas decimais

            return {
                distancia,
                duracao,
                origemLat: origemCoords.lat,
                origemLong: origemCoords.lon,
                destLat: destinoCoords.lat,
                destLong: destinoCoords.lon
            };

        } catch (error) {
            console.error("Erro ao calcular distância:", error);
            throw new Error("Erro ao calcular a distância entre os endereços.");
        }
    }

    /**
     * Converte um endereço em coordenadas geográficas (latitude e longitude).
     * @param endereco Endereço a ser convertido
     */
    private async geocodificarEndereco(endereco: string): Promise<{ lat: number; lon: number } | null> {
        try {
            const url = `${this.baseUrl}/search/address/json?api-version=1.0&subscription-key=${this.subscriptionKey}&query=${encodeURIComponent(endereco)}`;
            
            const response = await axios.get(url);
            const results = response.data.results;

            if (results.length === 0) return null;

            return {
                lat: results[0].position.lat,
                lon: results[0].position.lon,
            };
        } catch (error) {
            console.error("Erro ao geocodificar endereço:", error);
            return null;
        }
    }
}
