package com.lixb.mysms.entity.enums;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by Administrator on 2017/11/9.
 */

public class PriorityConverter implements PropertyConverter<Priority,Integer> {
    @Override
    public Priority convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        for (Priority priority:Priority.values()) {
            if (priority.id == databaseValue) {
                return priority;
            }
        }
        return Priority.DEFAULT;
    }

    @Override
    public Integer convertToDatabaseValue(Priority entityProperty) {
        return entityProperty == null ? null : entityProperty.id;
    }
}
