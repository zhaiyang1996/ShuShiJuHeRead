package com.shushijuhe.shushijuheread.dao;

import android.content.Context;
import android.util.Log;

import com.shushijuhe.shushijuheread.bean.BookData;
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.greendao.BookDataDao;
import com.shushijuhe.shushijuheread.greendao.BookMixATocLocalBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 目录数据库管理类
 */
public class BookMixATocLocalBeanDaoUtils {
    private static final String TAG = BookMixATocLocalBeanDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public BookMixATocLocalBeanDaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成BookshelfBean记录的插入，如果表未创建，先创建MBookshelfBean表
     * @param bookMixATocLocalBean
     * @return
     */
    public boolean insertBookMixATocLocalBean(BookMixATocLocalBean bookMixATocLocalBean){
        boolean flag = false;
        flag = mManager.getDaoSession().getBookMixATocLocalBeanDao().insert(bookMixATocLocalBean) == -1 ? false : true;
        Log.i(TAG, "insert Meizi :" + flag + "-->" + bookMixATocLocalBean.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param bookMixATocLocalBeanList
     * @return
     */
    public boolean insertMultBookMixATocLocalBean(final List<BookMixATocLocalBean> bookMixATocLocalBeanList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (BookMixATocLocalBean meizi : bookMixATocLocalBeanList) {
                        mManager.getDaoSession().insertOrReplace(meizi);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     * @param bookMixATocLocalBean
     * @return
     */
    public boolean updateBookMixATocLocalBean(BookMixATocLocalBean bookMixATocLocalBean){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(bookMixATocLocalBean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param bookMixATocLocalBean
     * @return
     */
    public boolean deleteBookMixATocLocalBean(BookMixATocLocalBean bookMixATocLocalBean){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(bookMixATocLocalBean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     * @return
     */
    public boolean deleteAll(){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(BookMixATocLocalBean.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<BookMixATocLocalBean> queryAllBookMixATocLocalBean(){
        return mManager.getDaoSession().loadAll(BookMixATocLocalBean.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public BookMixATocLocalBean queryBookMixATocLocalBeanById(long key){
        return mManager.getDaoSession().load(BookMixATocLocalBean.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<BookMixATocLocalBean> queryBookMixATocLocalBeanByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(BookMixATocLocalBean.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<BookMixATocLocalBean> queryBookMixATocLocalBeanByQueryBuilder(String id){
        QueryBuilder<BookMixATocLocalBean> queryBuilder = mManager.getDaoSession().queryBuilder(BookMixATocLocalBean.class);
        return queryBuilder.where(BookMixATocLocalBeanDao.Properties.Uid.eq(id)).list();
    }
}
