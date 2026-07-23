package com.bibliocanto.service;

import com.bibliocanto.dao.ExemplarLivroDAO;
import com.bibliocanto.dao.LivroDAO;
import com.bibliocanto.dto.ExemplarLivroDTO;
import com.bibliocanto.exception.EntityNotFoundException;
import com.bibliocanto.exception.ExemplarLivroNotFoundException;
import com.bibliocanto.model.ExemplarLivro;
import com.bibliocanto.model.Livro;
import com.bibliocanto.model.SituacaoLivro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExemplarLivroService {

    private final ExemplarLivroDAO exemplarDAO;
    private final LivroDAO livroDAO;

    public ExemplarLivroService(ExemplarLivroDAO exemplarDAO, LivroDAO livroDAO) {
        this.exemplarDAO = exemplarDAO;
        this.livroDAO = livroDAO;
    }

    @Transactional(readOnly = true)
    public List<ExemplarLivroDTO> buscarTodos() {
        return exemplarDAO.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExemplarLivroDTO buscarPorId(UUID id) {
        return convertToDTO(exemplarDAO.findById(id)
                .orElseThrow(() -> new ExemplarLivroNotFoundException("Exemplar não encontrado com ID: " + id)));
    }

    @Transactional
    public ExemplarLivroDTO criar(ExemplarLivroDTO dto) {
        try {
        if (dto.getIsbnLivro() <= 0) {
            throw new IllegalArgumentException("ISBN do livro inválido.");
        }



        // 2. Mapeia usando a ENTIDADE LIVRO, e não apenas o número
        ExemplarLivro exemplar = ExemplarLivro.builder()
                .isbn(dto.getIsbnLivro()) // <-- Passamos o Objeto Livro
                .situacao(dto.getSituacao())
                .build();

        exemplarDAO.save(exemplar);
        return convertToDTO(exemplar);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            throw e;
        }
    }

    @Transactional
    public ExemplarLivroDTO atualizarSituacao(UUID id, SituacaoLivro novaSituacao) {
        ExemplarLivro exemplarExistente = exemplarDAO.findById(id)
                .orElseThrow(() -> new ExemplarLivroNotFoundException("Exemplar não encontrado."));
        exemplarExistente.setSituacao(novaSituacao);
        return convertToDTO(exemplarDAO.update(exemplarExistente));
    }

    @Transactional
    public void deletar(UUID id) {
        try {
        ExemplarLivro exemplar = exemplarDAO.findById(id)
                .orElseThrow(() -> new ExemplarLivroNotFoundException("Exemplar não encontrado com ID: " + id));

        // Futuro: Verificar se a situação é 'EMPRESTADO' antes de deletar

        exemplarDAO.delete(exemplar);
        } catch (ExemplarLivroNotFoundException e) {
            // Repassa a exceção de negócio
            throw e;
        }
    }

    // --- Métodos Privados de Mapeamento ---

    private ExemplarLivroDTO convertToDTO(ExemplarLivro exemplar) {
        return ExemplarLivroDTO.builder()
                .id(exemplar.getId())
                .isbnLivro(exemplar.getIsbn())
                .situacao(exemplar.getSituacao())
                .idEmprestimo(exemplar.getIdEmprestimo())
                .build();
    }


    @Transactional
    public void exemplarEmprestado(UUID idExemplar, UUID idEmprestimo){
        ExemplarLivro exemplarExistente = exemplarDAO.findById(idExemplar)
                .orElseThrow(() -> new ExemplarLivroNotFoundException("Exemplar não encontrado."));
        exemplarExistente.setIdEmprestimo(idEmprestimo);
        exemplarDAO.update(exemplarExistente);

    }
}
