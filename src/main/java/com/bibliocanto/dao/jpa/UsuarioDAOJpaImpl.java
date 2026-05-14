package com.bibliocanto.dao.jpa;

import com.bibliocanto.dao.UsuarioDAO;
import com.bibliocanto.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioDAOJpaImpl implements UsuarioDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Usuario> findAll() {
        TypedQuery<Usuario> query = entityManager.createQuery("SELECT u FROM Usuario u", Usuario.class);
        return query.getResultList();
    }

    @Override
    public Optional<Usuario> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Usuario.class, id));
    }

    @Override
    public Optional<Usuario> findByLogin(String login) {
        TypedQuery<Usuario> query = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class);
        query.setParameter("login", login);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(Usuario usuario) {
        entityManager.persist(usuario);
    }

    @Override
    public Usuario update(Usuario usuario) {
        return entityManager.merge(usuario);
    }

    @Override
    public void delete(Usuario usuario) {
        if (!entityManager.contains(usuario)) {
            usuario = entityManager.merge(usuario);
        }
        entityManager.remove(usuario);
    }
}
