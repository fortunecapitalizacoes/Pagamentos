package br.com.loja.pagamentos.application.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoPagamentoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long idPedido;
	private String status;
}
