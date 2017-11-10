package com.lixb.mysms.entity.enums;

import com.lixb.mysms.entity.enums.TaskStatus;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by Administrator on 2017/11/9.
 */

public class TaskStatusConverter implements PropertyConverter<TaskStatus,Integer>{
    @Override
    public TaskStatus convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        for (TaskStatus ts : TaskStatus.values()) {
            if (databaseValue == ts.id) {
                return ts;
            }
        }
        return TaskStatus.NORMAL;
    }

    @Override
    public Integer convertToDatabaseValue(TaskStatus entityProperty) {
        return entityProperty == null ? null : entityProperty.id;
    }
}
