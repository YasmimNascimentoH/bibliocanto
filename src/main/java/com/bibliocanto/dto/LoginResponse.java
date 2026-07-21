package com.bibliocanto.dto;

import java.util.UUID;

public record LoginResponse(UUID id, String nome, String login) {
}
