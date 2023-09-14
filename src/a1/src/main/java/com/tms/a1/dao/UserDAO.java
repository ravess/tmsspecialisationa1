package com.tms.a1.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.tms.a1.entity.User;
import com.tms.a1.utils.HibernateUtil;

import jakarta.persistence.Query;

@Component
public class UserDAO {

  private HibernateUtil HibernateUtil;

    public void getUser(int id) {
        Transaction transaction = null;
        try (Session session = com.tms.a1.utils.HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            // get User entity using get() method
            User User = session.get(User.class, id);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    
    public void getUserById(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();

            // Obtain an entity using byId() method
            User User = session.byId(User.class).getReference(id);
         

            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void saveUser(User User) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the User object
            session.persist(User);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public String checkgroup(String username, String usergroup) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            String hql = "SELECT u.username FROM User u WHERE u.username = :username AND u.groups LIKE :userGroupPattern";
            Query query = session.createQuery(hql, String.class)
                .setParameter("username", username)
                .setParameter("userGroupPattern", "%." + usergroup + ".%");
            String result = (String) query.getSingleResult();
            // commit transaction
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }
}

