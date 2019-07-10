package com.lkc.model.userInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//联系方式表
@Entity
@Table(name = "contact_info")
public class UserContact {
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "email_ctl")
    private String email_ctl;

    @Column(name = "phone_ctl")
    private String phone_ctl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailctl() {
        return email_ctl;
    }

    public void setEmailctl(String email_ctl) {
        this.email_ctl = email_ctl;
    }

    public String getEmail_ctl() {
        return email_ctl;
    }

    public void setEmail_ctl(String email_ctl) {
        this.email_ctl = email_ctl;
    }
}
