package com.lixb.mysms.entity;

import com.lixb.mysms.entity.enums.Priority;
import com.lixb.mysms.entity.enums.PriorityConverter;
import com.lixb.mysms.entity.enums.TaskStatus;
import com.lixb.mysms.entity.enums.TaskStatusConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import java.util.List;

import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import com.lixb.mysms.db.greendao.DaoSession;
import com.lixb.mysms.db.greendao.TaskCommentDao;
import com.lixb.mysms.db.greendao.TaskDao;
import com.lixb.mysms.db.greendao.RepeatTaskInfoDao;

/**
 * Created by Administrator on 2017/11/8.
 * 任务
 */

@Entity
public class Task {

    public static final byte DEFAULT_SCORE = 1;

    @Id(autoincrement=true)
    private Long id;

    /**
     * 任务id标识
     */
    @Unique
    private String taskIdStr;
    /**
     * 用户id
     */
    private long userId;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务状态
     */
    @Convert(converter = TaskStatusConverter.class,columnType = Integer.class)
    private TaskStatus taskStatus;

    /**
     * 是否属于子任务
     */
    private boolean isSubTask;

    /**
     * 父任务id
     */
    private long parentId;

    /**
     * 优先级
     */
    @Convert(converter = PriorityConverter.class,columnType = Integer.class)
    private Priority priority;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date deadLine;

    /**
     * 完成时间
     */
    private Date completeDate;

    /**
     * 完成花费时间
     */
    private long completeSpendTime;

    /**
     * 任务创建时间
     */
    private Date createDate;

    /**
     * 最后一次操作时间
     */
    private Date lastOperateDate;


    /**
     * 评论信息
     */
    @ToMany(joinProperties = {@JoinProperty(name="taskIdStr",referencedName = "taskIdStr") })
    @OrderBy("commentDate DESC")
    private List<TaskComment> comments;

    /**
     * 分值
     */
    private byte score = DEFAULT_SCORE;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;

    @Generated(hash = 686196634)
    public Task(Long id, String taskIdStr, long userId, String title, String description,
            TaskStatus taskStatus, boolean isSubTask, long parentId, Priority priority,
            Date startDate, Date deadLine, Date completeDate, long completeSpendTime,
            Date createDate, Date lastOperateDate, byte score) {
        this.id = id;
        this.taskIdStr = taskIdStr;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.isSubTask = isSubTask;
        this.parentId = parentId;
        this.priority = priority;
        this.startDate = startDate;
        this.deadLine = deadLine;
        this.completeDate = completeDate;
        this.completeSpendTime = completeSpendTime;
        this.createDate = createDate;
        this.lastOperateDate = lastOperateDate;
        this.score = score;
    }

    @Generated(hash = 733837707)
    public Task() {
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

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public TaskStatus getTaskStatus() {
        return this.taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public boolean getIsSubTask() {
        return this.isSubTask;
    }

    public void setIsSubTask(boolean isSubTask) {
        this.isSubTask = isSubTask;
    }

    public long getParentId() {
        return this.parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDeadLine() {
        return this.deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Date getCompleteDate() {
        return this.completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public long getCompleteSpendTime() {
        return this.completeSpendTime;
    }

    public void setCompleteSpendTime(long completeSpendTime) {
        this.completeSpendTime = completeSpendTime;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastOperateDate() {
        return this.lastOperateDate;
    }

    public void setLastOperateDate(Date lastOperateDate) {
        this.lastOperateDate = lastOperateDate;
    }

    public byte getScore() {
        return this.score;
    }

    public void setScore(byte score) {
        this.score = score;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 998898465)
    public List<TaskComment> getComments() {
        if (comments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskCommentDao targetDao = daoSession.getTaskCommentDao();
            List<TaskComment> commentsNew = targetDao._queryTask_Comments(taskIdStr);
            synchronized (this) {
                if (comments == null) {
                    comments = commentsNew;
                }
            }
        }
        return comments;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 249603048)
    public synchronized void resetComments() {
        comments = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskIdStr='" + taskIdStr + '\'' +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", isSubTask=" + isSubTask +
                ", parentId=" + parentId +
                ", priority=" + priority +
                ", startDate=" + startDate +
                ", deadLine=" + deadLine +
                ", completeDate=" + completeDate +
                ", completeSpendTime=" + completeSpendTime +
                ", createDate=" + createDate +
                ", lastOperateDate=" + lastOperateDate +
                ", comments=" + comments +
                ", score=" + score +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                '}';
    }
}
