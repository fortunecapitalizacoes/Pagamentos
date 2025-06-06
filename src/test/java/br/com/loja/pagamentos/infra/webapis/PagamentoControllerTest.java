package br.com.loja.pagamentos.infra.webapis;

import br.com.loja.pagamentos.application.dtos.PagamentoPayloadDTO;
import br.com.loja.pagamentos.domain.DomainServices;
import br.com.loja.pagamentos.infra.async.PagamentoProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.loja.pagamentos.application.util.Constantes.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagamentoController.class)
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DomainServices domainService;

    @MockBean
    private PagamentoProducer pagamentoProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRegistrarPagamentoEfetuado() throws Exception {
        PagamentoPayloadDTO dto = new PagamentoPayloadDTO();
        dto.setIdPedido(1L);

        mockMvc.perform(post("/api/pagamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(content().string("Pagamento recebido com sucesso para o pedido ID: 1"));

        verify(domainService).alterarStatusPagamento(argThat(e -> e.getIdPedido() == 1L && e.getStatus().equals(PAGAMENTO_EFETUADO)));
        verify(pagamentoProducer).informarPagamento(any());
    }

    @Test
    void deveRegistrarPagamentoCancelado() throws Exception {
        PagamentoPayloadDTO dto = new PagamentoPayloadDTO();
        dto.setIdPedido(2L);

        mockMvc.perform(post("/api/pagamentos/cancelar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(content().string("Pagamento cancelado com sucesso para o pedido ID: 2"));

        verify(domainService).alterarStatusPagamento(argThat(e -> e.getIdPedido() == 2L && e.getStatus().equals(PAGAMENTO_CANCELADO)));
        verify(pagamentoProducer).informarPagamento(any());
    }

    @Test
    void deveRegistrarPagamentoPendente() throws Exception {
        PagamentoPayloadDTO dto = new PagamentoPayloadDTO();
        dto.setIdPedido(3L);

        mockMvc.perform(post("/api/pagamentos/pendente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(content().string("Pagamento pendente registrado com sucesso para o pedido ID: 3"));

        verify(domainService).alterarStatusPagamento(argThat(e -> e.getIdPedido() == 3L && e.getStatus().equals(PAGAMENTO_PENDENTE)));
        verify(pagamentoProducer).informarPagamento(any());
    }
}

