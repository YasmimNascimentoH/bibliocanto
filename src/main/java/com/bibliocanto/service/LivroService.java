package com.bibliocanto.service;

import com.bibliocanto.dao.LivroDAO;
import com.bibliocanto.dao.jpa.ExemplarLivroDAOJPAImpl;
import com.bibliocanto.dto.ExemplarLivroDTO;
import com.bibliocanto.exception.EntityNotFoundException;
import com.bibliocanto.model.ExemplarLivro;
import com.bibliocanto.model.Livro;
import com.bibliocanto.model.SituacaoLivro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivroService {

    private final LivroDAO livroDAO;
    private final ExemplarLivroService exemplarService;


    // Injeção via construtor (melhor prática)
    public LivroService(LivroDAO livroDAO, ExemplarLivroService exemplarService) {
        this.livroDAO = livroDAO;
        this.exemplarService = exemplarService;
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
        for(int i = livro.getQuantidadeExemplares(); i>0; i--){
            ExemplarLivroDTO exemplarDTO = ExemplarLivroDTO.builder()
                    .isbnLivro(livro.getIsbn())
                    .situacao(SituacaoLivro.DISPONIVEL)
                    .build();
            exemplarService.criar(exemplarDTO);
        }

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
        livroExistente.setQuantidadeExemplares(dadosAtualizados.getQuantidadeExemplares());

        // O Hibernate atualiza automaticamente ao final da transação devido ao estado Managed,
        // mas chamamos explicitamente o update (merge) para clareza do padrão DAO.
        return livroDAO.update(livroExistente);
    }

    @Transactional
    public void deletar(Long isbn) {
        Livro livro = buscarPorIsbn(isbn);
        List<ExemplarLivro> exemplares = this.exemplares(isbn);
        for(int i = livro.getQuantidadeExemplares()-1; i>=0; i--){
            exemplarService.deletar(exemplares.get(i).getId());
        }
        livroDAO.delete(livro);
    }

    public List<ExemplarLivro> exemplares(Long isnb){
        return this.buscarPorIsbn(isnb).getExemplares();
    }
}
