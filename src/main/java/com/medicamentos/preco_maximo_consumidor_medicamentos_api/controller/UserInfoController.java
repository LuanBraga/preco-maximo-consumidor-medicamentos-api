package com.medicamentos.preco_maximo_consumidor_medicamentos_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserInfoController {

    /**
     * Endpoint para retornar as informações (claims) do usuário autenticado.
     * O Spring Security injeta automaticamente o token JWT decodificado
     * no parâmetro 'jwt' anotado com @AuthenticationPrincipal.
     *
     * @param jwt O token JWT do usuário autenticado.
     * @return um ResponseEntity contendo um mapa com todas as claims do token.
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        // O método getClaims() retorna todas as informações contidas no payload do token
        Map<String, Object> claims = jwt.getClaims();
        return ResponseEntity.ok(claims);
    }
}