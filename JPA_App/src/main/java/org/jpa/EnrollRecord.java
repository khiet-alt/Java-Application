package org.jpa;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "enroll")
public class EnrollRecord implements Serializable {

    @Id
    private String user_id;
    @Id
    private String subject_id;
    @Id
    private Date dayEnroll;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public Date getDayEnroll() {
        return dayEnroll;
    }

    public void setDayEnroll(Date dayEnroll) {
        this.dayEnroll = dayEnroll;
    }
}
