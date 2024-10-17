# Usar uma imagem oficial do Node.js
FROM node:20-alpine

# Definir o diretório de trabalho
WORKDIR /usr/src/app


# Copiar o package.json e package-lock.json antes de instalar as dependências
# para aproveitar o cache de camadas do Docker
COPY package*.json ./

ENV NODE_ENV=prod

RUN rm -rf node_modules
# Instalar dependências de produção e remover as de desenvolvimento
RUN npm install

# Copiar o código da aplicação para o contêiner
COPY . .

# Compilar a aplicação (caso você use TypeScript ou tenha uma etapa de build)
RUN npm run build

# Expôr a porta da aplicação (ajuste para a porta que seu app usa)
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["npm", "start"]
