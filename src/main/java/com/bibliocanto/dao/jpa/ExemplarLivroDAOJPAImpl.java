package com.bibliocanto.dao.jpa;

import com.bibliocanto.dao.ExemplarLivroDAO;
import com.bibliocanto.model.ExemplarLivro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ExemplarLivroDAOJPAImpl implements ExemplarLivroDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ExemplarLivro> findAll() {
        TypedQuery<ExemplarLivro> query = entityManager.createQuery("SELECT e FROM ExemplarLivro e", ExemplarLivro.class);
        return query.getResultList();
    }

    @Override
    public Optional<ExemplarLivro> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(ExemplarLivro.class, id));
    }

    @Override
    public void save(ExemplarLivro exemplar) {
        entityManager.persist(exemplar);
    }

    @Override
    public ExemplarLivro update(ExemplarLivro exemplar) {
        return entityManager.merge(exemplar);
    }

    @Override
    public void delete(ExemplarLivro exemplar) {
        if (!entityManager.contains(exemplar)) {
            exemplar = entityManager.merge(exemplar);
        }
        entityManager.remove(exemplar);
    }
}
