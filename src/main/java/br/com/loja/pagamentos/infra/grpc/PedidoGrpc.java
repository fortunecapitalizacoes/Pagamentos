package br.com.loja.pagamentos.infra.grpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import net.devh.boot.grpc.client.inject.GrpcClient;
import br.com.loja.grpc.ValidaPedidoRequest;
import br.com.loja.grpc.ValidaPedidoResponse;
import br.com.loja.grpc.ValidaPedidoServiceGrpc;

/**
 * Componente responsável por realizar chamadas gRPC para o serviço remoto de validação de pedidos.
 * 
 * Utiliza o stub {@link ValidaPedidoServiceGrpc.ValidaPedidoServiceBlockingStub} injetado
 * via anotação {@code @GrpcClient}, fornecida pela biblioteca Spring Boot gRPC Starter.
 * 
 * Essa classe permite verificar, de forma síncrona, se um pedido existe em outro serviço da aplicação.
 * Também realiza log de todas as etapas do processo, facilitando o rastreamento em produção.
 * 
 * Exemplo de uso:
 * <pre>
 * boolean existe = pedidoGrpc.pedidoExiste(123L);
 * </pre>
 * 
 * @author
 */
@Slf4j
@Component
public class PedidoGrpc {

    /** Stub gRPC para comunicação com o serviço de validação de pedidos. */
    @GrpcClient("pedido-service")
    private ValidaPedidoServiceGrpc.ValidaPedidoServiceBlockingStub validaPedidoStub;

    /**
     * Verifica se o pedido existe usando chamada gRPC síncrona.
     *
     * @param pedidoId ID do pedido a ser verificado
     * @return {@code true} se o pedido existir no sistema remoto, {@code false} caso contrário ou em caso de erro
     */
    public boolean pedidoExiste(long pedidoId) {
        try {
            log.info("Enviando requisição gRPC para verificar existência do pedido ID: {}", pedidoId);

            ValidaPedidoRequest request = ValidaPedidoRequest.newBuilder()
                    .setId(pedidoId)
                    .build();

            ValidaPedidoResponse response = validaPedidoStub.retornaTrueSeExiste(request);
            log.info("Resposta recebida do gRPC: success={}", response.getSuccess());

            return response.getSuccess();
        } catch (Exception e) {
            log.error("Erro ao consultar serviço gRPC: {}", e.getMessage(), e);
            return false;
        }
    }
}
