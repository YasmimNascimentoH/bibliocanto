package com.bibliocanto.dao.jpa;

import com.bibliocanto.dao.EmprestimoDAO;
import com.bibliocanto.model.Emprestimo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EmprestimoDAOJpaImpl implements EmprestimoDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Emprestimo> findAll() {
        TypedQuery<Emprestimo> query = entityManager.createQuery("SELECT e FROM Emprestimo e", Emprestimo.class);
        return query.getResultList();
    }

    @Override
    public Optional<Emprestimo> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Emprestimo.class, id));
    }

    @Override
    public void save(Emprestimo emprestimo) {
        entityManager.persist(emprestimo);
    }

    @Override
    public Emprestimo update(Emprestimo emprestimo) {
        return entityManager.merge(emprestimo);
    }

    @Override
    public void delete(Emprestimo emprestimo) {
        if (!entityManager.contains(emprestimo)) {
            emprestimo = entityManager.merge(emprestimo);
        }
        entityManager.remove(emprestimo);
    }
}
