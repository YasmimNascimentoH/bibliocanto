package com.bibliocanto.service;

import com.bibliocanto.dao.ExemplarLivroDAO;
import com.bibliocanto.dto.ExemplarLivroDTO;
import com.bibliocanto.exception.ExemplarLivroNotFoundException;
import com.bibliocanto.model.ExemplarLivro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExemplarLivroService {

    private final ExemplarLivroDAO exemplarDAO;
    // Opcional: injetar LivroDAO aqui no futuro para validar se o Livro existe antes de criar o Exemplar

    public ExemplarLivroService(ExemplarLivroDAO exemplarDAO) {
        this.exemplarDAO = exemplarDAO;
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
        // Regra de negócio simples
        if (dto.getIsbnLivro() <= 0) {
            throw new IllegalArgumentException("ISBN do livro inválido.");
        }

        // Mapeamento DTO -> Entidade
        ExemplarLivro exemplar = ExemplarLivro.builder()
                .livroIsbn(dto.getIsbnLivro())
                .situacao(dto.getSituacao())
                .build();

        exemplarDAO.save(exemplar);
        return convertToDTO(exemplar);
    }

    @Transactional
    public ExemplarLivroDTO atualizar(UUID id, ExemplarLivroDTO dto) {
        ExemplarLivro exemplarExistente = exemplarDAO.findById(id)
                .orElseThrow(() -> new ExemplarLivroNotFoundException("Exemplar não encontrado com ID: " + id));

        // Atualiza apenas os campos permitidos
        exemplarExistente.setLivroIsbn(dto.getIsbnLivro());
        exemplarExistente.setSituacao(dto.getSituacao());

        return convertToDTO(exemplarDAO.update(exemplarExistente));
    }

    @Transactional
    public void deletar(UUID id) {
        ExemplarLivro exemplar = exemplarDAO.findById(id)
                .orElseThrow(() -> new ExemplarLivroNotFoundException("Exemplar não encontrado com ID: " + id));

        // Futuro: Verificar se a situação é 'EMPRESTADO' antes de deletar

        exemplarDAO.delete(exemplar);
    }

    // --- Métodos Privados de Mapeamento ---

    private ExemplarLivroDTO convertToDTO(ExemplarLivro exemplar) {
        return ExemplarLivroDTO.builder()
                .id(exemplar.getId())
                .isbnLivro(exemplar.getLivroIsbn())
                .situacao(exemplar.getSituacao())
                .build();
    }
}
