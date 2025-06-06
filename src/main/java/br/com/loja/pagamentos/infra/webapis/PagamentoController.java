package br.com.loja.pagamentos.infra.webapis;

import br.com.loja.pagamentos.application.dtos.EventoPagamentoDTO;
import br.com.loja.pagamentos.application.dtos.PagamentoPayloadDTO;
import static br.com.loja.pagamentos.application.util.Constantes.PAGAMENTO_EFETUADO;
import static br.com.loja.pagamentos.application.util.Constantes.PAGAMENTO_CANCELADO;
import static br.com.loja.pagamentos.application.util.Constantes.PAGAMENTO_PENDENTE;
import br.com.loja.pagamentos.domain.DomainServices;
import br.com.loja.pagamentos.infra.async.PagamentoProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST responsável por receber requisições relacionadas a pagamentos.
 * 
 * Exponibiliza endpoints para registrar pagamentos efetuados, cancelados ou pendentes
 * para pedidos na aplicação.
 * 
 * Utiliza {@link DomainServices} para alteração do status do pagamento no domínio
 * e {@link PagamentoProducer} para enviar eventos de pagamento para processamento assíncrono.
 * 
 * URLs base: /api/pagamentos
 * 
 * Endpoints:
 * - POST /api/pagamentos : registra pagamento efetuado
 * - POST /api/pagamentos/cancelar : registra cancelamento de pagamento
 * - POST /api/pagamentos/pendente : registra pagamento pendente
 * 
 * Todos os endpoints recebem um payload contendo o ID do pedido.
 * 
 * @author
 * Cleiton Cardoso Silva
 */
@RestController
@RequestMapping("/api/pagamentos")
@Slf4j
@AllArgsConstructor
public class PagamentoController {
	
	private final DomainServices domainService;
	private final PagamentoProducer pagamentoProducer;
	
    /**
     * Recebe uma requisição para registrar um pagamento efetuado.
     * 
     * @param dto objeto contendo dados do pagamento (ex: idPedido)
     * @return mensagem de sucesso com status HTTP 200 OK
     */
    @PostMapping
    public ResponseEntity<String> receberPagamento(@RequestBody PagamentoPayloadDTO dto) {
        log.info("Recebido pagamento para o pedido com ID: {}", dto.getIdPedido());

        EventoPagamentoDTO eventoPagamentoDTO = EventoPagamentoDTO.builder()
            .idPedido(dto.getIdPedido())
            .status(PAGAMENTO_EFETUADO)
            .build();

        domainService.alterarStatusPagamento(eventoPagamentoDTO);
        pagamentoProducer.informarPagamento(eventoPagamentoDTO);
        return ResponseEntity.ok("Pagamento recebido com sucesso para o pedido ID: " + dto.getIdPedido());
    }
    
    /**
     * Recebe uma requisição para registrar o cancelamento de um pagamento.
     * 
     * @param dto objeto contendo dados do pagamento (ex: idPedido)
     * @return mensagem de sucesso com status HTTP 200 OK
     */
    @PostMapping("/cancelar")
    public ResponseEntity<String> cancelarPagamento(@RequestBody PagamentoPayloadDTO dto) {
        log.info("Recebido cancelamento de pagamento para o pedido com ID: {}", dto.getIdPedido());

        EventoPagamentoDTO eventoPagamentoDTO = EventoPagamentoDTO.builder()
            .idPedido(dto.getIdPedido())
            .status(PAGAMENTO_CANCELADO)
            .build();

        domainService.alterarStatusPagamento(eventoPagamentoDTO);
        pagamentoProducer.informarPagamento(eventoPagamentoDTO);
        return ResponseEntity.ok("Pagamento cancelado com sucesso para o pedido ID: " + dto.getIdPedido());
    }
    
    /**
     * Recebe uma requisição para registrar um pagamento pendente.
     * 
     * @param dto objeto contendo dados do pagamento (ex: idPedido)
     * @return mensagem de sucesso com status HTTP 200 OK
     */
    @PostMapping("/pendente")
    public ResponseEntity<String> pendentePagamento(@RequestBody PagamentoPayloadDTO dto) {
        log.info("Pagamento pendente para o pedido com ID: {}", dto.getIdPedido());

        EventoPagamentoDTO eventoPagamentoDTO = EventoPagamentoDTO.builder()
            .idPedido(dto.getIdPedido())
            .status(PAGAMENTO_PENDENTE)
            .build();

        domainService.alterarStatusPagamento(eventoPagamentoDTO);
        pagamentoProducer.informarPagamento(eventoPagamentoDTO);
        return ResponseEntity.ok("Pagamento pendente registrado com sucesso para o pedido ID: " + dto.getIdPedido());
    }
    
    @GetMapping
    public ResponseEntity<?> listarPagamentos() {        
        return ResponseEntity.ok(domainService.listarPagamentos());
    }
    
}
