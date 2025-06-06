# Sistema de Processamento de Pagamentos

Este sistema representa uma arquitetura simplificada para um processo de pagamento, onde um cliente interage com um microserviço responsável pelo processamento.

![Diagrama da Arquitetura](https://raw.githubusercontent.com/fortunecapitalizacoes/Pagamentos/refs/heads/main/Pagamentos.jpg)

## Visão Geral

### Protocolos Utilizados

- **HTTP/HTTPS**: Comunicação síncrona entre serviços (ex: chamadas REST via API Gateway).
- **AMQP**: Protocolo usado pelo RabbitMQ para comunicação assíncrona via filas de mensagens.
- **TCP/IP**: Comunicação de baixo nível entre containers gerenciada pelo Docker.
- **gRPC/HTTP2**: Protocolo de comunicação para chamadas de procedimentos remotos.

### 1. Cliente
- **Tipo:** Pessoa ou Bot
- **Descrição:** Este cliente pode ser um usuário humano ou um agente automatizado que realiza operações no sistema.

### 2. Operações Realizadas pelo Cliente
- Pagar
- Cancelar
- Tornar pendente

### 3. Microserviço: `pagamento-app`
- **Tecnologia:** Spring Boot
- **Descrição:** Microserviço responsável pelo processamento de pagamentos.
- **Responsabilidades:**
  - Receber requisições do cliente
  - Processar operações de pagamento
  - Retornar status das transações

## Fluxo de Interação

1. O cliente envia uma solicitação de operação (pagar, cancelar, tornar pendente).
2. A solicitação é enviada ao microserviço `pagamento-app`.
3. O microserviço processa a solicitação e retorna a resposta correspondente ao cliente.

## Tecnologias Utilizadas
- Java
- Spring Boot
- Arquitetura baseada em microserviços
---

## 🚀 Como subir o projeto localmente

### Pré-requisitos

- Docker + Docker Compose instalados
- Java 17+ instalado

### Passo 1 – Clonar o repositório

```bash
git clone https://github.com/fortunecapitalizacoes/Pagamentos.git
cd Pagamentos
```

### Passo 2 – Subir a infraestrutura

O projeto já contém um arquivo `docker-compose.yml` na raiz, com toda a infraestrutura necessária (RabbitMQ, PostgreSQL, etc). Basta executar:

```bash
docker-compose up -d
```

Aguarde alguns segundos até que os contêineres estejam completamente iniciados.

### Passo 3 – Rodar a aplicação Spring Boot

Com os contêineres rodando, inicie o projeto Spring Boot:

```bash
./mvnw spring-boot:run
```

Ou, se estiver usando o Maven instalado:

```bash
mvn spring-boot:run
```

> O projeto será iniciado na porta padrão (geralmente `8081`, verifique em `application.yml`).

---

## 🔗 Dependência externa: Módulo de Pedidos

O serviço de pagamentos **depende do serviço de pedidos** para validar se o pagamento está sendo feito para um pedido existente.

### Como baixar e iniciar o módulo de pedidos:

```bash
git clone https://github.com/fortunecapitalizacoes/Pedidos.git
cd Pedidos
docker-compose up -d  # se o projeto também tiver infraestrutura via docker-compose
mvn spring-boot:run   # ou ./mvnw spring-boot:run
```

Certifique-se de que o módulo de pedidos esteja ativo **antes de realizar requisições de pagamento**.


