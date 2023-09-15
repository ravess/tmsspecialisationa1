package com.tms.a1.dao;




import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.tms.a1.entity.User;
import com.tms.a1.utils.HibernateUtil;

import jakarta.persistence.Query;

@Component
public class UserDAO {

  private HibernateUtil HibernateUtil;

    public User findByUsername(String username) {
        Transaction transaction = null;
        try (Session session = com.tms.a1.utils.HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            String hql = "FROM User u WHERE u.username = :username ";
            Query query = session.createQuery(hql, User.class)
                .setParameter("username", username);
                
            List<User> resultList =  query.getResultList();
            if (!resultList.isEmpty()) {
                User result = resultList.get(0); // Get the first result
                transaction.commit();
                return result;
            }else{
                transaction.commit();
                return null;
            }

            
            

            // transaction.commit();
            // return result.get(0);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
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

    public List<User> findAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the User object
            Query query = session.createQuery("FROM User", User.class);
            List<User> users = query.getResultList();
            // commit transaction
            transaction.commit();
            return users;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    public List checkgroup(String username, String usergroup) {
        Transaction transaction = null;
        try (Session session = com.tms.a1.utils.HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            String hql = "SELECT u.username FROM User u WHERE u.username = :username AND u.groups LIKE :userGroupPattern";
            Query query = session.createQuery(hql, String.class)
                .setParameter("username", username)
                .setParameter("userGroupPattern", "%." + usergroup + ".%");
            List result =  query.getResultList();
            // commit transaction
            
      
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }
}

