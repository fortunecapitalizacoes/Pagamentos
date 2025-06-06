# Sistema de Processamento de Pagamentos

Este sistema representa uma arquitetura simplificada para um processo de pagamento, onde um cliente interage com um microserviço responsável pelo processamento.

## Visão Geral

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

## Observações
Este diagrama é útil para representar uma arquitetura inicial de comunicação entre cliente e serviço de pagamentos em um sistema orientado a microsserviços.

---

Desenvolvido com base em um modelo visual (draw.io / diagrams.net).
