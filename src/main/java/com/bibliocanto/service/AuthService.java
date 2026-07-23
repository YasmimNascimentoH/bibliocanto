package com.bibliocanto.service;

import com.bibliocanto.dao.LoginDAO;
import com.bibliocanto.dto.LoginRequest;
import com.bibliocanto.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final LoginDAO loginDAO;

    public LoginResponse login(LoginRequest request) {
        System.out.println(request.email()+"  --- "+ request.senha());
        return loginDAO.findByEmailAndSenha(request.email(), request.senha())
                .map(user -> new LoginResponse(user.getId(), user.getNome(), user.getEmail(), user.getTipo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos"));
    }
}
