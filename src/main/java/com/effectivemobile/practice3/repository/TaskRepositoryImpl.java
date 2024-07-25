package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.model.entity.Task;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRespository<Task> {

    private final SessionFactory sessionFactory;

    public TaskRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Task> save(Task task) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.persist(task);
        transaction.commit();
        return Optional.ofNullable(task);
    }

    @Override
    public void deleteById(Long id) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.createMutationQuery("delete from task where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        transaction.commit();
    }

    @Override
    public Optional<Task> findById(Long id) {
        Task task;
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        task = session.get(Task.class, id);
        transaction.commit();
        return Optional.ofNullable(task);
    }

    @Override
    public Optional<Task> findByTitle(String title) {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query<Task> query = session.createQuery("from task u where u.title=:title", Task.class);
        query.setParameter("title", title);
        Task task = query.uniqueResult();
        transaction.commit();
        return Optional.ofNullable(task);
    }

    public List<Task> findAll() {
        List<Task> list;
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        list = session.createQuery("from task ", Task.class).list();
        transaction.commit();
        return list;
    }

    @Override
    public void deleteAll() {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.createMutationQuery("delete from task")
                .executeUpdate();
        transaction.commit();
    }

    private Session getCurrentSession() {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }
        return session;
    }

}
