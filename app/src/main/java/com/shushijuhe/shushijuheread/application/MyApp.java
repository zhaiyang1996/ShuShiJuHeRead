package com.shushijuhe.shushijuheread.application;

import android.app.Application;

import com.shushijuhe.shushijuheread.constants.Constants;
import com.shushijuhe.shushijuheread.greendao.DaoMaster;
import com.shushijuhe.shushijuheread.greendao.DaoSession;

/**
 * autor:ziv
 * date:2018/6/5
 * 初始化数据库
 */
public class MyApp extends Application {
    private static DaoSession daoSessionBooksheif;

    @Override
    public void onCreate() {
        super.onCreate();
        MarketDao();
    }

    //加载数据库MarketDao工具
    private void MarketDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), Constants.DAO_BOOKSHELF, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSessionBooksheif = daoMaster.newSession();
    }


    public static DaoSession getDaoSessiondaoBooksheif() {
        return daoSessionBooksheif;
    }


}
