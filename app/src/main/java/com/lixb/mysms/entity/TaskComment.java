package com.lixb.mysms.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/9.
 * 任务评论列表
 */
@Entity
public class TaskComment {
    @Id(autoincrement = true)
    private Long id;

    private String taskIdStr;

    private String content;

    private Date commentDate;

    @Generated(hash = 247797966)
    public TaskComment(Long id, String taskIdStr, String content,
            Date commentDate) {
        this.id = id;
        this.taskIdStr = taskIdStr;
        this.content = content;
        this.commentDate = commentDate;
    }

    @Generated(hash = 681491428)
    public TaskComment() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskIdStr() {
        return this.taskIdStr;
    }

    public void setTaskIdStr(String taskIdStr) {
        this.taskIdStr = taskIdStr;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentDate() {
        return this.commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }


}
