package com.bibliocanto.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "exemplares_livro")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
// Configura ToString para ignorar campos Lazy (se houver no futuro) e focar no ID
@ToString(onlyExplicitlyIncluded = true)
// Configura Equals/HashCode para usar APENAS o ID UUID para identidade
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExemplarLivro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    @EqualsAndHashCode.Include // Inclui explicitamente o ID no Equals/HashCode e ToString
    @ToString.Include
    private UUID id;

    // Relacionamento N:1 (Vários Exemplares pertencem a Um Livro)
    // Cria a Foreign Key 'livro_isbn' no banco de dados apontando para 'livros.isbn'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro", nullable = false)
    private Livro livro;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private SituacaoLivro situacao;

    /* O 'mappedBy' indica que a FK (exemplar_id) fica na tabela de Empréstimos
    @OneToOne(mappedBy = "exemplar", fetch = FetchType.LAZY)
    private Emprestimo emprestimo;*/
}
