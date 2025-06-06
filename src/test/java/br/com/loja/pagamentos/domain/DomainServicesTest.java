package br.com.loja.pagamentos.domain;


import br.com.loja.pagamentos.application.dtos.EventoPagamentoDTO;
import br.com.loja.pagamentos.application.exceptions.PedidoNaoEncontradoException;
import br.com.loja.pagamentos.domain.entities.PagamentoEntity;
import br.com.loja.pagamentos.infra.grpc.PedidoGrpc;
import br.com.loja.pagamentos.infra.repositories.PagamentoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DomainServicesTest {

    @Mock
    private PedidoGrpc pedidoGrpc;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private DomainServices domainServices;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void alterarStatusPagamento_DeveSalvarPagamento_QuandoPedidoExiste() {
        // Arrange
        long idPedido = 123L;
        EventoPagamentoDTO dto = EventoPagamentoDTO.builder()
                .idPedido(idPedido)
                .status("EFETUADO")
                .build();

        when(pedidoGrpc.pedidoExiste(idPedido)).thenReturn(true);
        when(pagamentoRepository.save(any(PagamentoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        domainServices.alterarStatusPagamento(dto);

        // Assert
        verify(pedidoGrpc, times(1)).pedidoExiste(idPedido);
        verify(pagamentoRepository, times(1)).save(argThat(p -> 
            p.getIdPedido() == idPedido && p.getStatus().equals(dto.getStatus())
        ));
    }

    @Test
    void alterarStatusPagamento_DeveLancarExcecao_QuandoPedidoNaoExiste() {
        // Arrange
        long idPedido = 456L;
        EventoPagamentoDTO dto = EventoPagamentoDTO.builder()
                .idPedido(idPedido)
                .status("EFETUADO")
                .build();

        when(pedidoGrpc.pedidoExiste(idPedido)).thenReturn(false);

        // Act & Assert
        PedidoNaoEncontradoException exception = assertThrows(
                PedidoNaoEncontradoException.class,
                () -> domainServices.alterarStatusPagamento(dto)
        );

        assertEquals("Pedido com id 456 não encontrado.", exception.getMessage());

        verify(pagamentoRepository, never()).save(any());
    }
}