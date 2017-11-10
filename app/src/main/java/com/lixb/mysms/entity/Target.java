package com.lixb.mysms.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/8.
 * 目标
 */
@Entity
public class Target {
    @Id(autoincrement=true)
    private Long id;

    private String title;

    private String description;

    @Generated(hash = 101006905)
    public Target(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Generated(hash = 231566653)
    public Target() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
