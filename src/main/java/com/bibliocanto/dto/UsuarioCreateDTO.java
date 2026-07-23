package com.bibliocanto.dto;

import com.bibliocanto.model.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UsuarioCreateDTO extends UsuarioDTO{

    private TipoUsuario tipo;
}
