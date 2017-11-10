package com.lixb.mysms.entity.enums;

/**
 * Created by Administrator on 2017/11/9.
 * 任务优先级枚举
 */

public enum Priority {
    DEFAULT(0), LOW(1),MIDDLE(2),HIGH(3);
    final int id;
    Priority(int id){
        this.id = id;
    }
}
