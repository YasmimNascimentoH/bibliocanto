package com.bibliocanto.dto;

import com.bibliocanto.model.TipoUsuario;

import java.util.UUID;

public record LoginResponse(UUID id, String nome, String login, TipoUsuario tipo) {
}
