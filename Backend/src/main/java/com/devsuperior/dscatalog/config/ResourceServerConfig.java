package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private JwtTokenStore jwtTokenStore;

    //Configurando um vetor com as rotas e quais roles poderão ser acesso.
    public static final String[] PUBLIC = {"/oauth/token"}; //Login -> Liberado a todos.
    public static final String[] OPERATOR_OR_ADMIN = {"/products/**", "/categories/**"};
    public static final String[] ADMIN = {"/user/**"};

    //Método que valida o token recebido.
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(jwtTokenStore);
    }

    //Definindo/Configurando as rotas, permissões e roles.
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers(PUBLIC).permitAll()
                //Liberando as requisições do tipo GET para products e categories. -> Mas se fizer o login as outras funcionam... ?
                .antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()
                //Definindo as roles dos usuários que conseguirão acessar essas rotas.
                .antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN") //hasAnyRole libera várias roles.
                .antMatchers(ADMIN).hasRole("ADMIN") //hasRole libera somente uma role.
                //Definindo que qualquer outra rota não especificada precisa estar autenticado(logado).
                .anyRequest().authenticated();
    }
}