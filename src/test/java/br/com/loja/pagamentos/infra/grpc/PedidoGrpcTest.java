package br.com.loja.pagamentos.infra.grpc;

import br.com.loja.grpc.ValidaPedidoRequest;
import br.com.loja.grpc.ValidaPedidoResponse;
import br.com.loja.grpc.ValidaPedidoServiceGrpc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoGrpcTest {

    private PedidoGrpc pedidoGrpc;
    private ValidaPedidoServiceGrpc.ValidaPedidoServiceBlockingStub validaPedidoStub;

    @BeforeEach
    void setUp() throws Exception {
        pedidoGrpc = new PedidoGrpc();
        validaPedidoStub = mock(ValidaPedidoServiceGrpc.ValidaPedidoServiceBlockingStub.class);

        // Injeta o mock no campo privado via reflexão
        Field stubField = PedidoGrpc.class.getDeclaredField("validaPedidoStub");
        stubField.setAccessible(true);
        stubField.set(pedidoGrpc, validaPedidoStub);
    }

    @Test
    void pedidoExiste_DeveRetornarTrue_QuandoServicoResponderComSucessoTrue() {
        // Arrange
        long pedidoId = 123L;
        ValidaPedidoResponse response = ValidaPedidoResponse.newBuilder()
                .setSuccess(true)
                .build();

        when(validaPedidoStub.retornaTrueSeExiste(any(ValidaPedidoRequest.class))).thenReturn(response);

        // Act
        boolean existe = pedidoGrpc.pedidoExiste(pedidoId);

        // Assert
        assertTrue(existe);
        verify(validaPedidoStub).retornaTrueSeExiste(any(ValidaPedidoRequest.class));
    }

    @Test
    void pedidoExiste_DeveRetornarFalse_QuandoServicoResponderComSucessoFalse() {
        // Arrange
        long pedidoId = 456L;
        ValidaPedidoResponse response = ValidaPedidoResponse.newBuilder()
                .setSuccess(false)
                .build();

        when(validaPedidoStub.retornaTrueSeExiste(any(ValidaPedidoRequest.class))).thenReturn(response);

        // Act
        boolean existe = pedidoGrpc.pedidoExiste(pedidoId);

        // Assert
        assertFalse(existe);
    }

    @Test
    void pedidoExiste_DeveRetornarFalse_QuandoOcorrerExcecao() {
        // Arrange
        when(validaPedidoStub.retornaTrueSeExiste(any())).thenThrow(new RuntimeException("Erro gRPC"));

        // Act
        boolean existe = pedidoGrpc.pedidoExiste(789L);

        // Assert
        assertFalse(existe);
    }
}