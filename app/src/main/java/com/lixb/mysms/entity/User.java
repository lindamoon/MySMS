package com.lixb.mysms.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;


/**
 * Created by Administrator on 2017/11/8.
 * 个人信息
 */

@Entity
public class User {
    @Id(autoincrement=true)
    private Long id;
    private String name;
    private String age;
    private String sex;
    private String email;
    private String mobilephone;
    private Date regDate;
    private long score;
    @Generated(hash = 1267357419)
    public User(Long id, String name, String age, String sex, String email,
            String mobilephone, Date regDate, long score) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.mobilephone = mobilephone;
        this.regDate = regDate;
        this.score = score;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobilephone() {
        return this.mobilephone;
    }
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }
    public Date getRegDate() {
        return this.regDate;
    }
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    public long getScore() {
        return this.score;
    }
    public void setScore(long score) {
        this.score = score;
    }


}
