package com.zhai.shuyangwx.dao;

import android.content.Context;
import android.util.Log;

import com.zhai.shuyangwx.bean.BookshelfBean;
import com.zhai.shuyangwx.greendao.BookshelfBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 书架数据库管理
 */
public class BookshelfBeanDaoUtils {
    private static final String TAG = BookshelfBeanDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public BookshelfBeanDaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成BookshelfBean记录的插入，如果表未创建，BookshelfBean
     * @param bookshelfBean
     * @return
     */
    public boolean insertBookshelfBean(BookshelfBean bookshelfBean){
        boolean flag = false;
        flag = mManager.getDaoSession().getBookshelfBeanDao().insert(bookshelfBean) == -1 ? false : true;
        Log.i(TAG, "insert Meizi :" + flag + "-->" + bookshelfBean.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param bookshelfBeanList
     * @return
     */
    public boolean insertMultBookshelfBean(final List<BookshelfBean> bookshelfBeanList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (BookshelfBean meizi : bookshelfBeanList) {
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
     * @param bookshelfBean
     * @return
     */
    public boolean updateBookshelfBean(BookshelfBean bookshelfBean){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(bookshelfBean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param bookshelfBean
     * @return
     */
    public boolean deleteBookshelfBean(BookshelfBean bookshelfBean){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(bookshelfBean);
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
            mManager.getDaoSession().deleteAll(BookshelfBean.class);
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
    public List<BookshelfBean> queryAllBookshelfBean(){
        return mManager.getDaoSession().loadAll(BookshelfBean.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public BookshelfBean queryBookshelfBeanById(long key){
        return mManager.getDaoSession().load(BookshelfBean.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<BookshelfBean> queryBookshelfBeanByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(BookshelfBean.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<BookshelfBean> queryBookshelfBeanByQueryBuilder(String id){
        QueryBuilder<BookshelfBean> queryBuilder = mManager.getDaoSession().queryBuilder(BookshelfBean.class);
        return queryBuilder.where(BookshelfBeanDao.Properties.BookId.eq(id)).list();
    }

    /**
     * 使用queryBuilder按时间排序进行查询
     * @return
     */
    public List<BookshelfBean> queryBookshelfBeanByQueryBuilder_TimeA(){
        QueryBuilder<BookshelfBean> queryBuilder = mManager.getDaoSession().queryBuilder(BookshelfBean.class);
        return queryBuilder.orderDesc(BookshelfBeanDao.Properties.TimeMillis).list();
    }

    /**
     * 关闭数据库
     */
    public void closeConnection(){
        mManager.closeConnection();
    }
}
