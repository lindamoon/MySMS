package com.lixb.mysms.entity.enums;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by Administrator on 2017/11/9.
 */

public class RepeatModeConverter implements PropertyConverter<RepeatMode,Integer>{
    @Override
    public RepeatMode convertToEntityProperty(Integer databaseValue) {
        if (null == databaseValue) {
            return null;
        }
        for (RepeatMode rm : RepeatMode.values()) {
            if (rm.id == databaseValue) {
                return rm;
            }
        }
        return RepeatMode.NOREPEAT;
    }

    @Override
    public Integer convertToDatabaseValue(RepeatMode entityProperty) {
        return entityProperty == null ? null : entityProperty.id;
    }
}
