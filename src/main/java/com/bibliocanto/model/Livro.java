package com.bibliocanto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;




    @Entity
    @Table(name = "livros")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Livro {

        @Id
        private Long isbn; // Identificador natural/universal

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

}
