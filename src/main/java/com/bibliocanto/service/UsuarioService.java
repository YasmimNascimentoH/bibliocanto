package com.bibliocanto.service;

import com.bibliocanto.dao.UsuarioDAO;
import com.bibliocanto.dto.UsuarioDTO;
import com.bibliocanto.exception.UsuarioNotFoundException;
import com.bibliocanto.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    // Removido o PasswordEncoder do construtor
    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarTodos() {
        return usuarioDAO.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(UUID id) {
        return convertToDTO(usuarioDAO.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado: " + id)));
    }

    @Transactional
    public UsuarioDTO criar(UsuarioDTO dto) {
        // Validação manual de senha obrigatória na criação
        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new RuntimeException("Senha é obrigatória para criação de usuário.");
        }

        // Verificar login único
        if (usuarioDAO.findByLogin(dto.getLogin()).isPresent()) {
            throw new RuntimeException("Login já está em uso: " + dto.getLogin());
        }

        // Mapear DTO -> Entidade
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .login(dto.getLogin())
                .senha(dto.getSenha()) // Salva em texto
                .tipo(dto.getTipo())
                .build();

        usuarioDAO.save(usuario);
        return convertToDTO(usuario);
    }

    @Transactional
    public UsuarioDTO atualizar(UUID id, UsuarioDTO dto) {
        Usuario usuarioExistente = usuarioDAO.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado: " + id));

        // Verificar unicidade do login se mudou
        if (!usuarioExistente.getLogin().equals(dto.getLogin()) &&
                usuarioDAO.findByLogin(dto.getLogin()).isPresent()) {
            throw new RuntimeException("Login já está em uso: " + dto.getLogin());
        }

        // Atualizar campos comuns
        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setLogin(dto.getLogin());
        usuarioExistente.setTipo(dto.getTipo());

        // Atualizar senha diretamente apenas se uma nova foi fornecida
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuarioExistente.setSenha(dto.getSenha()); // Salva texto claro
        }

        return convertToDTO(usuarioDAO.update(usuarioExistente));
    }

    @Transactional
    public void deletar(UUID id) {
        Usuario usuario = usuarioDAO.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado: " + id));
        usuarioDAO.delete(usuario);
    }

    // Helper para converter entidade para o DTO Único de resposta
    private UsuarioDTO convertToDTO(Usuario usuario) {
        // Devido ao @JsonProperty(access = WRITE_ONLY) no DTO, o Jackson
        // ignorará o campo 'senha' no JSON de resposta, mesmo se preenchermos aqui.
        // Definimos explicitamente como null para maior clareza.
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .login(usuario.getLogin())
                .tipo(usuario.getTipo())
                .senha(null) // Não enviamos a senha
                .build();
    }
}
