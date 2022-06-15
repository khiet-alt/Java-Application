package operation;

import org.jpa.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Crud {

    static public int login(String account, String password){
        /*
        * Return 1: Student
        * Return 0: Teacher
        * Return -1: False
        * */
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        TypedQuery<User> query = em.createQuery("SELECT c FROM User c WHERE c.username = :username", User.class);
        List<User> listUsers = query.setParameter("username", account).getResultList();

        if (listUsers == null || listUsers.size() == 0){
            return -1;
        }

        User user = listUsers.get(0);


        if (user.isFirst() && user.getRole() != 0){
            return 1;
        }

        em.getTransaction().commit();

        if (user.getPassword().equals(EncryptPass.encryptPassword(password))){
            if (user.getRole() == 0)
                return 0;
            else if (user.getRole() == 1)
                return 1;
        }
        return -1;
    }

    static public User getUser(String name, String pass){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        TypedQuery<User> query = em.createQuery("SELECT c FROM User c WHERE c.username = :username", User.class);
        List<User> listUsers = query.setParameter("username", name).getResultList();

        User user = listUsers.get(0);

        em.getTransaction().commit();

        return user;
    }

    static public List<Subject> getSubjectOfUser(String userid){
        List<Subject> list = null;
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createNativeQuery("select *\n" +
                "from subject\n" +
                "where id in (select subject_id from user_subject where user_id = :userid)", Subject.class);
        list = query.setParameter("userid", userid).getResultList();

        em.getTransaction().commit();

        return list;
    }

    static public List<User> getListStudent(){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        List<User> users = null;
        users = em.createQuery("SELECT c FROM User c WHERE c.role = 1", User.class)
                .getResultList();

        em.getTransaction().commit();
        return users;
    }

    static public List<User> getListStudentOfSubject(String subject_id){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        List<User> users = null;
        users = em.createNativeQuery("select u.*\n" +
                        "from user u\n" +
                        "where u.id in (\n" +
                        "\tselect u2.id\n" +
                        "    from user u2 join user_subject us on u2.id = us.user_id\n" +
                        "    where us.subject_id = :subject_id" +
                        ")", User.class)
                .setParameter("subject_id", subject_id)
                .getResultList();

        em.getTransaction().commit();
        return users;
    }

    static public boolean registerUser(String id, String username, String pass, int role, String email, String phone){
        EntityManager em = EntityInstance.getEntityManager();
        String hashPass = EncryptPass.encryptPassword(pass);

        System.out.println(username);

        em.getTransaction().begin();
        List<User> users = null;
        users = em.createQuery("SELECT c FROM User c WHERE c.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        if (users.size() != 0){
            return false;
        }

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(hashPass);
        user.setRole(role);
        user.setEmail(email);
        user.setPhone(phone);
        user.setFirst(true);

        em.persist(user);

        em.getTransaction().commit();
        return true;
    }

    static public void updateUser(User user, String newPassword){
        EntityManager em = EntityInstance.getEntityManager();
        String hashPass = EncryptPass.encryptPassword(newPassword);

        em.getTransaction().begin();

        Query query = em.createNativeQuery("Update User Set isFirst=False, password=:hashPass WHERE id = :id", User.class)
                .setParameter("hashPass", hashPass)
                .setParameter("id", user.getId());

        query.executeUpdate();

        em.getTransaction().commit();
    }

    static public List<Subject> listSubject(){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        List<Subject> subjects = null;
        subjects = em.createQuery("SELECT c FROM Subject c", Subject.class)
                .getResultList();

        em.getTransaction().commit();

        return subjects;
    }

    static public Subject getOneSubject(String id){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Subject sub = null;
        Query query = em.createQuery("SELECT c FROM Subject c Where c.id = :id", Subject.class);
        query.setParameter("id", id);
        sub = (Subject) query.getSingleResult();

        em.getTransaction().commit();

        return sub;
    }

    static public void addSubject(String id, String name, String roomName, int dayOfWeek, String startDay, String endDay, String startHour, String endHour) throws ParseException {
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");

        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        subject.setRoomName(roomName);
        subject.setDayOfWeek(dayOfWeek);
        subject.setStartDay(dayFormatter.parse(startDay));
        subject.setEndDay(dayFormatter.parse(endDay));
        subject.setStartHour(hourFormatter.parse(startHour));
        subject.setEndHour(hourFormatter.parse(endHour));

        em.persist(subject);

        em.getTransaction().commit();
    }

    static public void removeSubject(String id){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createQuery("DELETE FROM Subject where id = :id");
        query.setParameter("id", id);

        query.executeUpdate();

        em.getTransaction().commit();
    }

    static public void enrollSubject(String user_id, String subject_id){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createNativeQuery("INSERT INTO user_subject VALUES(:userid, :subjectid)");
        query.setParameter("userid", user_id);
        query.setParameter("subjectid", subject_id);

        query.executeUpdate();

        em.getTransaction().commit();
    }

    static public List<UserSubject> getUserEnroll(String subject_id){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createQuery("Select us from user_subject us where us.subject_id = :subject_id", UserSubject.class);
        query.setParameter("subject_id", subject_id);

        List<UserSubject> list = (List<UserSubject>) query.getResultList();

        em.getTransaction().commit();
        return list;
    }

    static public List<User> getUserExcept(String current_subject){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createNativeQuery("select u.*\n" +
                "from user u\n" +
                "where u.role = 1 and u.id not in (select u2.id\n" +
                "from user u2 join user_subject us on u2.id = us.user_id\n" +
                "                    where us.subject_id = :current_subject\n" +
                ")", User.class);
        query.setParameter("current_subject", current_subject);

        List<User> listUser = (List<User>) query.getResultList();

        em.getTransaction().commit();

        return listUser;
    }

    static public void addSubjectUser(String user, String subject){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createNativeQuery("Insert Into user_subject Values (:user_id, :subject_id)");
        query.setParameter("user_id", user);
        query.setParameter("subject_id", subject);
        query.executeUpdate();

        em.getTransaction().commit();
    }

    static public void createEnrollRecord(String user_id, String subject_id, ArrayList<String> dayEnroll) throws ParseException {
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy");
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        for (String day: dayEnroll){
            EnrollRecord enrollRecord = new EnrollRecord();
            enrollRecord.setUser_id(user_id);
            enrollRecord.setSubject_id(subject_id);
            enrollRecord.setDayEnroll(dayFormatter.parse(day));

            em.persist(enrollRecord);
        }

        em.getTransaction().commit();
    }

    static public List<EnrollRecord> getEnrollRecord(String subject_id){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        List<EnrollRecord> list;

        list = em.createQuery("Select c From enroll c where c.subject_id = :subject_id", EnrollRecord.class)
                        .setParameter("subject_id", subject_id)
                                .getResultList();

        em.getTransaction().commit();
        return list;
    }

    static public List<EnrollRecord> getEnrollRecordOfStudent(String userid, String subject_id){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        List<EnrollRecord> list;

        list = em.createQuery("Select c From enroll c where c.subject_id = :subject_id and c.user_id = :user_id", EnrollRecord.class)
                .setParameter("subject_id", subject_id)
                .setParameter("user_id", userid)
                .getResultList();

        em.getTransaction().commit();
        return list;
    }

    static public void deleteAllEnrollRecord(String subject_id){
        EntityManager em = EntityInstance.getEntityManager();

        em.getTransaction().begin();

        Query query = em.createQuery("Delete From enroll where subject_id = :subject_id");
        query.setParameter("subject_id", subject_id);
        query.executeUpdate();

        em.getTransaction().commit();
    }

}