package br.com.loja.pagamentos.application.exceptions;

public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(Long idPedido) {
        super("Pedido com id " + idPedido + " não encontrado.");
    }
}