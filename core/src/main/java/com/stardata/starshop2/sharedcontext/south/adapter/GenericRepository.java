package com.stardata.starshop2.sharedcontext.south.adapter;

import com.stardata.starshop2.sharedcontext.domain.AggregateRoot;
import com.stardata.starshop2.sharedcontext.domain.Identity;
import com.stardata.starshop2.sharedcontext.exception.InitializedEntityManagerException;
import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.TransactionScoped;
import java.util.Collection;
import java.util.List;


/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/9 20:35
 */
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

    public List<E> findByIds(Collection<ID> ids)  {
        requireEntityManagerNotNull();


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);

        CriteriaBuilder.In<ID> in = criteriaBuilder.in(root.get("id"));
        ids.forEach(in::value);
        criteriaQuery.where(new Predicate[]{in});

        TypedQuery<E> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    public List<E> findAll()  {
        requireEntityManagerNotNull();
        CriteriaQuery<E> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        return entityManager.createQuery(query).getResultList();
    }

    public List<E> findAll(int pageNumber, int pageSize)  {
        requireEntityManagerNotNull();

        pageNumber = pageNumber - 1;
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 100;
        }
        CriteriaQuery<E> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        return entityManager.createQuery(query)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
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

    public List<E> findBy(Specification<E> specification, int pageNumber, int pageSize) {
        requireEntityManagerNotNull();
        if (specification == null) {
            return findAll();
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);

        pageNumber = pageNumber - 1;
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 100;
        }

        Predicate predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(new Predicate[]{predicate});

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
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
