package com.lixb.mysms.entity.enums;

/**
 * Created by Administrator on 2017/11/9.
 */

public enum RepeatMode {
    NOREPEAT(0),EVERYDAY(1),EVERYWEEK(2),EVERYMONTH(3),EVERYYEAR(4);
    final int id;
    RepeatMode(int id){
        this.id = id;
    }
}
