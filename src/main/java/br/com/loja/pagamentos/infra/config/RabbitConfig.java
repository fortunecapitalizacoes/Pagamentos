package br.com.loja.pagamentos.infra.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * Classe de configuração do RabbitMQ.
 * 
 * Define a fila, exchange, binding, conversor de mensagens e o template de envio
 * de mensagens com suporte a JSON.
 * 
 * Essa configuração permite que a aplicação envie e receba mensagens usando
 * RabbitMQ com integração via Spring AMQP.
 * 
 * @author
 * Cleiton Cardoso Silva
 */
@Configuration
public class RabbitConfig {

    /** Nome da fila de pagamento */
    public static final String QUEUE_NAME = "fila_pagamento";

    /** Nome da exchange utilizada para rotear mensagens */
    public static final String EXCHANGE_NAME = "pedidos-exchange";

    /** Chave de roteamento usada para vincular fila e exchange */
    public static final String ROUTING_KEY = "pedidos.key";

    /**
     * Declara a fila com o nome especificado.
     * 
     * @return uma instância de {@link Queue} com durabilidade ativada.
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // durable = true
    }

    /**
     * Declara uma exchange do tipo Direct.
     * 
     * @return uma instância de {@link DirectExchange}.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * Define o binding entre a fila e a exchange usando a routing key.
     * 
     * @param queue a fila declarada.
     * @param exchange a exchange declarada.
     * @return uma instância de {@link Binding}.
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * Define um conversor de mensagens para JSON, utilizando a biblioteca Jackson.
     * 
     * @return instância de {@link Jackson2JsonMessageConverter}.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Cria e configura o {@link RabbitTemplate} para envio de mensagens,
     * utilizando o conversor JSON.
     * 
     * @param connectionFactory fábrica de conexões com o RabbitMQ.
     * @param converter conversor de mensagens para JSON.
     * @return uma instância de {@link RabbitTemplate} configurada.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

}
