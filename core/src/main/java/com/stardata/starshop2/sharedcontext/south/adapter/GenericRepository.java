package com.stardata.starshop2.sharedcontext.south.adapter;

import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.Identity;
import com.stardata.starshop2.sharedcontext.exception.InitializedEntityManagerException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.TransactionScoped;
import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/9 20:35
 */
@Component
@TransactionScoped
public class GenericRepository<E extends AggregateRoot, ID extends Identity> {
    private final Class<E> entityClass;
    private final EntityManager entityManager;

    public GenericRepository(Class<E> entityClass, EntityManager entityManager) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    public E findById(ID id)  {
        requireEntityManagerNotNull();

        return  entityManager.find(entityClass, id);
    }

    public List<E> findAll()  {
        requireEntityManagerNotNull();
        CriteriaQuery<E> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        return entityManager.createQuery(query).getResultList();
    }

    public List<E> findBy(Specification<E> specification) {
        requireEntityManagerNotNull();
        if (specification == null) {
            return findAll();
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);

        Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(new Predicate[]{predicate});

        TypedQuery<E> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    public void saveOrUpdate(E entity) {
        requireEntityManagerNotNull();

        if (entity == null) {
            return;
        }

        if (entityManager.contains(entity)) {
            entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
    }

    public void delete(E entity){
        requireEntityManagerNotNull();

        if (entity == null) {
            return;
        }

        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        }
    }


    @SneakyThrows
    private void requireEntityManagerNotNull()  {
        if (this.entityManager == null) {
            throw new InitializedEntityManagerException("Repository must have entityManager");
        }

    }
}
