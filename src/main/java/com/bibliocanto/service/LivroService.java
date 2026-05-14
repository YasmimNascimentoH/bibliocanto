package com.bibliocanto.service;

import com.bibliocanto.dao.LivroDAO;
import com.bibliocanto.exception.EntityNotFoundException;
import com.bibliocanto.model.Livro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroDAO livroDAO;

    // Injeção via construtor (melhor prática)
    public LivroService(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    // Consultas podem ser somente leitura para otimização
    @Transactional(readOnly = true)
    public List<Livro> buscarTodos() {
        return livroDAO.findAll();
    }

    @Transactional(readOnly = true)
    public Livro buscarPorIsbn(Long isbn) {
        return livroDAO.findById(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Livro não encontrado com ISBN: " + isbn));
    }

    @Transactional // Inicia uma transação de escrita
    public void criar(Livro livro) {
        // Lógica de negócio: verificar se ISBN já existe, etc.
        if (livroDAO.findById(livro.getIsbn()).isPresent()) {
            throw new RuntimeException("Já existe um livro cadastrado com este ISBN.");
        }
        livroDAO.save(livro);
    }

    @Transactional
    public Livro atualizar(Long isbn, Livro dadosAtualizados) {
        // Recupera o livro existente (anexando ao EntityManager)
        Livro livroExistente = buscarPorIsbn(isbn);

        // Atualiza os dados (Java 21 não muda muito aqui, a menos que use Records para DTOs)
        livroExistente.setTitulo(dadosAtualizados.getTitulo());
        livroExistente.setAutor(dadosAtualizados.getAutor());
        livroExistente.setEditora(dadosAtualizados.getEditora());
        livroExistente.setAnoPublicacao(dadosAtualizados.getAnoPublicacao());
        livroExistente.setNumeroPaginas(dadosAtualizados.getNumeroPaginas());
        livroExistente.setAssunto(dadosAtualizados.getAssunto());
        livroExistente.setSinopse(dadosAtualizados.getSinopse());

        // O Hibernate atualiza automaticamente ao final da transação devido ao estado Managed,
        // mas chamamos explicitamente o update (merge) para clareza do padrão DAO.
        return livroDAO.update(livroExistente);
    }

    @Transactional
    public void deletar(Long isbn) {
        Livro livro = buscarPorIsbn(isbn);
        livroDAO.delete(livro);
    }
}
