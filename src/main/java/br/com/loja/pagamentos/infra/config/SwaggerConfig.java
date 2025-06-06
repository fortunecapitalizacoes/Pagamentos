package br.com.loja.pagamentos.infra.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Pedidos")
                .description("API REST para gerenciamento de pedidos e itens")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Equipe Loja")
                    .email("contato@loja.com.br")
                    .url("https://loja.com.br"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")));
    }
}