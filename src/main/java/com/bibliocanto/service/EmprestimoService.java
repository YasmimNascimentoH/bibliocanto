package com.bibliocanto.service;

import com.bibliocanto.dao.EmprestimoDAO;
import com.bibliocanto.dto.EmprestimoDTO;
import com.bibliocanto.exception.EmprestimoNotFoundException;
import com.bibliocanto.model.Emprestimo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO;

    public EmprestimoService(EmprestimoDAO emprestimoDAO) {
        this.emprestimoDAO = emprestimoDAO;
    }

    @Transactional(readOnly = true)
    public List<EmprestimoDTO> buscarTodos() {
        return emprestimoDAO.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmprestimoDTO buscarPorId(UUID id) {
        return convertToDTO(emprestimoDAO.findById(id)
                .orElseThrow(() -> new EmprestimoNotFoundException("Empréstimo não encontrado com ID: " + id)));
    }

    @Transactional
    public EmprestimoDTO criar(EmprestimoDTO dto) {
        // Validação de negócio (Exemplo: não pode devolver no passado)
        if (dto.getDataDevolucaoPrevista().isBefore(dto.getDataRetirada())) {
            throw new IllegalArgumentException("A data de devolução não pode ser anterior à data de retirada.");
        }

        Emprestimo emprestimo = Emprestimo.builder()
                .exemplarId(dto.getExemplarId())
                .visitanteId(dto.getVisitanteId())
                .dataRetirada(dto.getDataRetirada())
                .dataDevolucaoPrevista(dto.getDataDevolucaoPrevista())
                .dataDevolucaoReal(dto.getDataDevolucaoReal())
                .situacao(dto.getSituacao())
                .build();

        emprestimoDAO.save(emprestimo);
        return convertToDTO(emprestimo);
    }

    @Transactional
    public EmprestimoDTO atualizar(UUID id, EmprestimoDTO dto) {
        Emprestimo emprestimoExistente = emprestimoDAO.findById(id)
                .orElseThrow(() -> new EmprestimoNotFoundException("Empréstimo não encontrado com ID: " + id));

        emprestimoExistente.setExemplarId(dto.getExemplarId());
        emprestimoExistente.setVisitanteId(dto.getVisitanteId());
        emprestimoExistente.setDataRetirada(dto.getDataRetirada());
        emprestimoExistente.setDataDevolucaoPrevista(dto.getDataDevolucaoPrevista());
        emprestimoExistente.setDataDevolucaoReal(dto.getDataDevolucaoReal());
        emprestimoExistente.setSituacao(dto.getSituacao());

        return convertToDTO(emprestimoDAO.update(emprestimoExistente));
    }

    @Transactional
    public void deletar(UUID id) {
        Emprestimo emprestimo = emprestimoDAO.findById(id)
                .orElseThrow(() -> new EmprestimoNotFoundException("Empréstimo não encontrado com ID: " + id));

        emprestimoDAO.delete(emprestimo);
    }

    // --- Mapeamento Manual ---
    private EmprestimoDTO convertToDTO(Emprestimo emprestimo) {
        return EmprestimoDTO.builder()
                .id(emprestimo.getId())
                .exemplarId(emprestimo.getExemplarId())
                .visitanteId(emprestimo.getVisitanteId())
                .dataRetirada(emprestimo.getDataRetirada())
                .dataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista())
                .dataDevolucaoReal(emprestimo.getDataDevolucaoReal())
                .situacao(emprestimo.getSituacao())
                .build();
    }
}
