package com.bibliocanto.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "exemplares_livro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Configura ToString para ignorar campos Lazy (se houver no futuro) e focar no ID
@ToString(onlyExplicitlyIncluded = true)
// Configura Equals/HashCode para usar APENAS o ID UUID para identidade
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExemplarLivro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    @EqualsAndHashCode.Include // Inclui explicitamente o ID no Equals/HashCode e ToString
    @ToString.Include
    private UUID id;

    @Column(name = "livro_isbn", nullable = false)
    private long livroIsbn;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private SituacaoLivro situacao;
}
