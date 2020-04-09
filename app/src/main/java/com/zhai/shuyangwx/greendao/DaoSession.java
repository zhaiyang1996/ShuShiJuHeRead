package com.zhai.shuyangwx.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.zhai.shuyangwx.bean.BookData;
import com.zhai.shuyangwx.bean.BookMixATocLocalBean;
import com.zhai.shuyangwx.bean.BookReadHistory;
import com.zhai.shuyangwx.bean.BookshelfBean;

import com.zhai.shuyangwx.greendao.BookDataDao;
import com.zhai.shuyangwx.greendao.BookMixATocLocalBeanDao;
import com.zhai.shuyangwx.greendao.BookReadHistoryDao;
import com.zhai.shuyangwx.greendao.BookshelfBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bookDataDaoConfig;
    private final DaoConfig bookMixATocLocalBeanDaoConfig;
    private final DaoConfig bookReadHistoryDaoConfig;
    private final DaoConfig bookshelfBeanDaoConfig;

    private final BookDataDao bookDataDao;
    private final BookMixATocLocalBeanDao bookMixATocLocalBeanDao;
    private final BookReadHistoryDao bookReadHistoryDao;
    private final BookshelfBeanDao bookshelfBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bookDataDaoConfig = daoConfigMap.get(BookDataDao.class).clone();
        bookDataDaoConfig.initIdentityScope(type);

        bookMixATocLocalBeanDaoConfig = daoConfigMap.get(BookMixATocLocalBeanDao.class).clone();
        bookMixATocLocalBeanDaoConfig.initIdentityScope(type);

        bookReadHistoryDaoConfig = daoConfigMap.get(BookReadHistoryDao.class).clone();
        bookReadHistoryDaoConfig.initIdentityScope(type);

        bookshelfBeanDaoConfig = daoConfigMap.get(BookshelfBeanDao.class).clone();
        bookshelfBeanDaoConfig.initIdentityScope(type);

        bookDataDao = new BookDataDao(bookDataDaoConfig, this);
        bookMixATocLocalBeanDao = new BookMixATocLocalBeanDao(bookMixATocLocalBeanDaoConfig, this);
        bookReadHistoryDao = new BookReadHistoryDao(bookReadHistoryDaoConfig, this);
        bookshelfBeanDao = new BookshelfBeanDao(bookshelfBeanDaoConfig, this);

        registerDao(BookData.class, bookDataDao);
        registerDao(BookMixATocLocalBean.class, bookMixATocLocalBeanDao);
        registerDao(BookReadHistory.class, bookReadHistoryDao);
        registerDao(BookshelfBean.class, bookshelfBeanDao);
    }
    
    public void clear() {
        bookDataDaoConfig.clearIdentityScope();
        bookMixATocLocalBeanDaoConfig.clearIdentityScope();
        bookReadHistoryDaoConfig.clearIdentityScope();
        bookshelfBeanDaoConfig.clearIdentityScope();
    }

    public BookDataDao getBookDataDao() {
        return bookDataDao;
    }

    public BookMixATocLocalBeanDao getBookMixATocLocalBeanDao() {
        return bookMixATocLocalBeanDao;
    }

    public BookReadHistoryDao getBookReadHistoryDao() {
        return bookReadHistoryDao;
    }

    public BookshelfBeanDao getBookshelfBeanDao() {
        return bookshelfBeanDao;
    }

}