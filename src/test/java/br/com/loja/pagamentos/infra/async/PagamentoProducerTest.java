package br.com.loja.pagamentos.infra.async;

import br.com.loja.pagamentos.application.dtos.EventoPagamentoDTO;
import br.com.loja.pagamentos.domain.DomainServices;
import br.com.loja.pagamentos.infra.config.RabbitConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PagamentoProducerTest {

    private RabbitTemplate rabbitTemplate;
    private DomainServices domainServices;
    private PagamentoProducer pagamentoProducer;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        domainServices = mock(DomainServices.class);
        pagamentoProducer = new PagamentoProducer(rabbitTemplate, domainServices);
    }

    @Test
    void informarPagamento_DeveChamarServicosCorretamente() {
        // Arrange
        EventoPagamentoDTO dto = EventoPagamentoDTO.builder()
                .idPedido(123L)
                .status("EFETUADO")
                .build();

        // Act
        pagamentoProducer.informarPagamento(dto);

        // Assert
        verify(domainServices).alterarStatusPagamento(dto);

        // Captura os argumentos passados para o método convertAndSend
        ArgumentCaptor<EventoPagamentoDTO> captor = ArgumentCaptor.forClass(EventoPagamentoDTO.class);
        verify(rabbitTemplate).convertAndSend(eq(RabbitConfig.EXCHANGE_NAME), eq(RabbitConfig.ROUTING_KEY), captor.capture());

        EventoPagamentoDTO enviado = captor.getValue();
        assertEquals(dto.getIdPedido(), enviado.getIdPedido());
        assertEquals(dto.getStatus(), enviado.getStatus());
    }
}