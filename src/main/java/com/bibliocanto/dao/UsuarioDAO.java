package com.bibliocanto.dao;

import com.bibliocanto.model.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioDAO {
    List<Usuario> findAll();
    Optional<Usuario> findById(UUID id);
    Optional<Usuario> findByLogin(String login); // Para verificar unicidade
    void save(Usuario usuario);
    Usuario update(Usuario usuario);
    void delete(Usuario usuario);
}
