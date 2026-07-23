package com.bibliocanto.service;

import com.bibliocanto.dao.EmprestimoDAO;
import com.bibliocanto.dto.EmprestimoDTO;
import com.bibliocanto.dto.ExemplarLivroDTO;
import com.bibliocanto.exception.EmprestimoNotFoundException;
import com.bibliocanto.model.Emprestimo;
import com.bibliocanto.model.SituacaoEmprestimo;
import com.bibliocanto.model.SituacaoLivro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO;
    private final ExemplarLivroService exemplarService;

    public EmprestimoService(EmprestimoDAO emprestimoDAO, ExemplarLivroService exemplarService) {
        this.emprestimoDAO = emprestimoDAO;
        this.exemplarService = exemplarService;
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

        Emprestimo emprestimo = Emprestimo.builder()
                .exemplarId(dto.getExemplarId())
                .visitanteId(dto.getVisitanteId())
                .dataRetirada(LocalDate.now())
                .dataDevolucaoPrevista(LocalDate.now().plusDays(7))
                .dataDevolucaoReal(dto.getDataDevolucaoReal())
                .situacao(SituacaoEmprestimo.EMPRESTADO)
                .build();

        emprestimoDAO.save(emprestimo);
        exemplarService.atualizarSituacao(emprestimo.getExemplarId(),SituacaoLivro.EMPRESTADO);
        exemplarService.exemplarEmprestado(emprestimo.getExemplarId(), emprestimo.getId());

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

    @Transactional
    public long devolucaoEmprestimo(UUID idExemplar) {

        ExemplarLivroDTO exemplar = exemplarService.buscarPorId(idExemplar);
        UUID id = exemplar.getIdEmprestimo();

        EmprestimoDTO dto = this.buscarPorId(id);
        Emprestimo emprestimoExistente = emprestimoDAO.findById(id)
                .orElseThrow(() -> new EmprestimoNotFoundException("Empréstimo não encontrado com ID: " + id));

        emprestimoExistente.setExemplarId(dto.getExemplarId());
        emprestimoExistente.setVisitanteId(dto.getVisitanteId());
        emprestimoExistente.setDataRetirada(dto.getDataRetirada());
        emprestimoExistente.setDataDevolucaoPrevista(dto.getDataDevolucaoPrevista());
        emprestimoExistente.setDataDevolucaoReal(LocalDate.now());
        emprestimoExistente.setSituacao(SituacaoEmprestimo.DEVOLVIDO);
        emprestimoDAO.update(emprestimoExistente);
        exemplarService.atualizarSituacao(exemplar.getId(), SituacaoLivro.DISPONIVEL);

        long diasAtraso = ChronoUnit.DAYS.between(dto.getDataDevolucaoPrevista(),LocalDate.now());
        return diasAtraso > 0 ? diasAtraso : 0;
    }

    @Transactional
    public List<EmprestimoDTO> emprestimosEsuario(UUID idUsuario){
        List<EmprestimoDTO> emprestimos = buscarTodos();
        List<EmprestimoDTO> emprestimoUsuario = new ArrayList<>();

        for(int i = emprestimos.size()-1; i>=0; i--){
            if (emprestimos.get(i).getVisitanteId().equals(idUsuario))
                emprestimoUsuario.add(emprestimos.get(i));

        }
        return emprestimoUsuario;
    }

}
