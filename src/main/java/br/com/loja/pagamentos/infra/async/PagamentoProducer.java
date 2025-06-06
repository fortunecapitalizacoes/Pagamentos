package br.com.loja.pagamentos.infra.async;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import br.com.loja.pagamentos.application.dtos.EventoPagamentoDTO;
import br.com.loja.pagamentos.domain.DomainServices;
import br.com.loja.pagamentos.infra.config.RabbitConfig;
import lombok.AllArgsConstructor;

/**
 * Componente responsável por produzir e enviar mensagens de pagamento
 * para o broker RabbitMQ.
 * 
 * Essa classe utiliza o {@link RabbitTemplate} para publicar eventos
 * de pagamento na exchange definida em {@link RabbitConfig}.
 * Antes de enviar a mensagem, também atualiza o status do pagamento
 * através do serviço de domínio {@link DomainServices}.
 * 
 * @author  
 */
@Component
@AllArgsConstructor
public class PagamentoProducer {

    private final RabbitTemplate rabbitTemplate;
    private final DomainServices domainServices;

    /**
     * Informa o pagamento enviando um evento para a fila configurada no RabbitMQ.
     * 
     * O método realiza duas ações principais:
     * 1. Atualiza o status do pagamento por meio da camada de domínio.
     * 2. Envia o DTO para a exchange usando a routing key configurada.
     * 
     * @param dto Objeto {@link EventoPagamentoDTO} contendo os dados do pagamento.
     */
    public void informarPagamento(EventoPagamentoDTO dto) {
        domainServices.alterarStatusPagamento(dto);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, dto);
        System.out.println("Mensagem enviada para a fila: " + dto);
    }
}
