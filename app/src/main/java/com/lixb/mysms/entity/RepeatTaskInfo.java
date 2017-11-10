package com.lixb.mysms.entity;

import com.lixb.mysms.entity.enums.RepeatMode;
import com.lixb.mysms.entity.enums.RepeatModeConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/9.
 * 要重复的任务信息
 */
@Entity
public class RepeatTaskInfo {
    @Id(autoincrement = true)
    private Long id;

    /**
     * 任务id标识
     */
    private String taskIdStr;

    /**
     * 重复模式
     */
    @Convert(converter = RepeatModeConverter.class,columnType = Integer.class)
    private RepeatMode repeatMode;

    /**
     * 重复策略
     */
    private String repeatStrategy;

    /**
     * 已经重复了多少次
     */
    private long repeatCount;

    /**
     * 最后一次完成的时间
     */
    private Date lastCompletedDate;

    @Generated(hash = 126798651)
    public RepeatTaskInfo(Long id, String taskIdStr, RepeatMode repeatMode,
            String repeatStrategy, long repeatCount, Date lastCompletedDate) {
        this.id = id;
        this.taskIdStr = taskIdStr;
        this.repeatMode = repeatMode;
        this.repeatStrategy = repeatStrategy;
        this.repeatCount = repeatCount;
        this.lastCompletedDate = lastCompletedDate;
    }

    @Generated(hash = 1241772113)
    public RepeatTaskInfo() {
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

    public RepeatMode getRepeatMode() {
        return this.repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    public String getRepeatStrategy() {
        return this.repeatStrategy;
    }

    public void setRepeatStrategy(String repeatStrategy) {
        this.repeatStrategy = repeatStrategy;
    }

    public long getRepeatCount() {
        return this.repeatCount;
    }

    public void setRepeatCount(long repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Date getLastCompletedDate() {
        return this.lastCompletedDate;
    }

    public void setLastCompletedDate(Date lastCompletedDate) {
        this.lastCompletedDate = lastCompletedDate;
    }

}
