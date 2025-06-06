package br.com.loja.pagamentos.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.loja.pagamentos.application.dtos.EventoPagamentoDTO;
import br.com.loja.pagamentos.application.exceptions.PedidoNaoEncontradoException;
import static br.com.loja.pagamentos.application.util.Constantes.PAGAMENTO_EFETUADO;

import br.com.loja.pagamentos.domain.entities.PagamentoEntity;
import br.com.loja.pagamentos.infra.async.PagamentoProducer;
import br.com.loja.pagamentos.infra.grpc.PedidoGrpc;
import br.com.loja.pagamentos.infra.repositories.PagamentoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Serviço responsável pelas regras de negócio relacionadas a pagamentos.
 */
@Service
@AllArgsConstructor
@Slf4j
public class DomainServices {

    private final PedidoGrpc RPC;
    private final PagamentoRepository pagamentoRepository;
    /**
     * Efetua o pagamento de um pedido, persistindo o status no banco de dados
     * e publicando uma mensagem na fila para processamento assíncrono.
     *
     * @param idPedido ID do pedido que será pago.
     * @throws PedidoNaoEncontradoException se o pedido não existir.
     */
    public void alterarStatusPagamento(EventoPagamentoDTO pagamentoDTO) {
        log.info("Iniciando processamento de pagamento para o pedido ID: {}", pagamentoDTO.getIdPedido());

        Optional.ofNullable(RPC.pedidoExiste(pagamentoDTO.getIdPedido()))
           .filter(existe -> existe)
           .orElseThrow(() -> {
               log.warn("Pedido com ID {} não encontrado", pagamentoDTO.getIdPedido());
               return new PedidoNaoEncontradoException(pagamentoDTO.getIdPedido());
           });

        log.info("Pedido com ID {} encontrado. Efetuando pagamento...", pagamentoDTO.getIdPedido());

        pagamentoRepository.save(PagamentoEntity.builder()
                                                .idPedido(pagamentoDTO.getIdPedido())
                                                .status(pagamentoDTO.getStatus())
                                                .build());

        log.info("Pagamento registrado com sucesso no banco de dados para o pedido ID: {}", pagamentoDTO.getIdPedido());

        log.info("Mensagem de pagamento enviado com sucesso para a fila. Pedido ID: {}", pagamentoDTO.getIdPedido());
    }
    
    public List<PagamentoEntity> listarPagamentos() {
    	return pagamentoRepository.findAll();
    }
}
