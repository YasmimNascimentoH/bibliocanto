package com.bibliocanto.dto;

import com.bibliocanto.model.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
// Inclui campos nulos na resposta (para que a senha=null apareça se desejado),
// ou use NON_NULL para ocultar completamente campos vazios na resposta.
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UsuarioDTO {

    // Campo de resposta (Response), ignorado na criação/edição
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    // Campos comuns (Request e Response)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Login é obrigatório")
    private String email;


    // Campo sensível:
    // Mesmo em texto claro, não queremos enviar a senha de volta no JSON.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha; // Senha bruta na Request, null na Response
}
