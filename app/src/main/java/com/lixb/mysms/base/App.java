package com.lixb.mysms.base;

import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.lixb.mysms.db.greendao.DaoMaster;
import com.lixb.mysms.db.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Administrator on 2017/11/7.
 */

public class App extends MultiDexApplication {

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;
    private final String TAG = getClass().getSimpleName();
    private static App sApp;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        Utils.init(this);
        initDatabase();
    }

    private void initDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "sms-db-encrypted" : "sms-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("haha") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static App getApplication() {
        return sApp;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
