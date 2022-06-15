package org.example.dboperation;

import org.example.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class DbOperation {

    static public boolean login(String account, String password){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        TypedQuery<User> query = em.createQuery("SELECT c FROM User c WHERE c.username = :username", User.class);
        List<User> listUsers = query.setParameter("username", account).getResultList();

        if (listUsers == null || listUsers.size() == 0){
            return false;
        }

        User user = listUsers.get(0);

        em.getTransaction().commit();

        if (user.getPassword().equals(EncryptPass.encryptPassword(password))){
            return true;
        }
        return false;
    }

    static public boolean registerUser(String username, String pass){
        EntityManager em = EntityInstance.getEntityManager();
        String hashPass = EncryptPass.encryptPassword(pass);

        em.getTransaction().begin();
        List<User> users = null;
        users = em.createQuery("SELECT c FROM User c WHERE c.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        if (users.size() != 0){
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPass);

        em.persist(user);

        em.getTransaction().commit();
        return true;
    }
}
