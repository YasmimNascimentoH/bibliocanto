package com.bibliocanto.dto;

import com.bibliocanto.model.SituacaoEmprestimo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "O ID do exemplar é obrigatório")
    private UUID exemplarId;

    @NotNull(message = "O ID do visitante é obrigatório")
    private UUID visitanteId;

    @NotNull(message = "A data de retirada é obrigatória")
    private LocalDate dataRetirada;

    @NotNull(message = "A data de devolução prevista é obrigatória")
    private LocalDate dataDevolucaoPrevista;

    private LocalDate dataDevolucaoReal;

    @NotNull(message = "A situação do empréstimo é obrigatória")
    private SituacaoEmprestimo situacao;
}
