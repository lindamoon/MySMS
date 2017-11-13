package com.lixb.mysms.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/13.
 * 完成任务历史记录
 */

@Entity
public class DoHistory {
    @Id(autoincrement = true)
    private Long id;
    private Long userId;
    private String userName;
    private String taskIdStr;
    private String taskTitle;
    private Date completedDate;
    @Generated(hash = 1005543276)
    public DoHistory(Long id, Long userId, String userName, String taskIdStr,
            String taskTitle, Date completedDate) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.taskIdStr = taskIdStr;
        this.taskTitle = taskTitle;
        this.completedDate = completedDate;
    }
    @Generated(hash = 874244660)
    public DoHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getTaskIdStr() {
        return this.taskIdStr;
    }
    public void setTaskIdStr(String taskIdStr) {
        this.taskIdStr = taskIdStr;
    }
    public String getTaskTitle() {
        return this.taskTitle;
    }
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
    public Date getCompletedDate() {
        return this.completedDate;
    }
    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
