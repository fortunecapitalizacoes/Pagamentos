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

