package com.bibliocanto.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    // Relacionamentos mapeados pelo ID (para manter o padrão DAO simples)
    @Column(name = "exemplar_id", nullable = false)
    private UUID exemplarId;

    @Column(name = "visitante_id", nullable = false)
    private UUID visitanteId;

    @Column(name = "data_retirada", nullable = false)
    private LocalDate dataRetirada;

    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDevolucaoPrevista;

    @Column(name = "data_devolucao_real")
    private LocalDate dataDevolucaoReal; // Pode ser nulo até o livro ser devolvido

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private SituacaoEmprestimo situacao;
}
