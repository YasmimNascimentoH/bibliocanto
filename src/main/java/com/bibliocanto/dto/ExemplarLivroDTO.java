package com.bibliocanto.dto;

import com.bibliocanto.model.SituacaoLivro;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExemplarLivroDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "O ISBN do livro é obrigatório")
    private long isbnLivro; // Nome seguindo o diagrama UML

    @NotNull(message = "A situação do livro é obrigatória")
    private SituacaoLivro situacao;
}
