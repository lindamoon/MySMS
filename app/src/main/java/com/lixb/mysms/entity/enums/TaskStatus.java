package com.lixb.mysms.entity.enums;

/**
 * Created by Administrator on 2017/11/9.
 * 任务状态
 */

public enum TaskStatus {
    NORMAL(0),FINISHED(1),UNFINISHED(2),OVERTIME(3),CLOSED(4), MODIFYABLE(5);
    public final int id;
    TaskStatus(int id){
        this.id = id;
    }
}
