package com.bibliocanto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

import java.util.List;


    @Entity
    @Table(name = "livros")
    @Data
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @ToString(onlyExplicitlyIncluded = true)
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public class Livro {

        @Id
        @EqualsAndHashCode.Include
        @ToString.Include
        private Long isbn; //Long para suportar 13 dígitos

        @Column(nullable = false)
        private String titulo;

        @Column(nullable = false)
        private String autor;

        @Column
        private String editora;

        @Column(name = "ano_publicacao")
        private LocalDate anoPublicacao;

        @Column(name = "numero_paginas")
        private Integer numeroPaginas;

        @Column
        private String assunto;

        @Column(columnDefinition = "TEXT")
        private String sinopse;

        // "mappedBy" indica que a tabela de exemplares é quem guarda a FK
        @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<ExemplarLivro> exemplares;

}
