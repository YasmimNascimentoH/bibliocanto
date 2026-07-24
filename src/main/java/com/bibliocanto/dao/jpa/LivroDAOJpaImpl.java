package com.bibliocanto.dao.jpa;

import com.bibliocanto.dao.LivroDAO;
import com.bibliocanto.model.Livro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LivroDAOJpaImpl implements LivroDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Livro> findAll() {
        // Usando JPQL clássico
        TypedQuery<Livro> query = entityManager.createQuery("SELECT l FROM Livro l", Livro.class);
        return query.getResultList();
    }

    @Override
    public Optional<Livro> findById(Long isbn) {
        // Retorna Optional para compatibilidade com o Service moderno
        return Optional.ofNullable(entityManager.find(Livro.class, isbn));
    }

    @Override
    public void save(Livro livro) {
        // Persiste uma nova entidade
        entityManager.persist(livro);
    }

    @Override
    public Livro update(Livro livro) {
        // Mescla o estado destacado de volta ao contexto de persistência
        return entityManager.merge(livro);
    }

    @Override
    public void delete(Livro livro) {
        // É necessário garantir que a entidade esteja anexada antes de remover
        if (!entityManager.contains(livro)) {
            livro = entityManager.merge(livro);
        }
        entityManager.remove(livro);
    }
}
