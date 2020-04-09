package com.zhai.shuyangwx.dao;

import android.content.Context;
import android.util.Log;

import com.zhai.shuyangwx.bean.BookData;
import com.zhai.shuyangwx.bean.BookReadHistory;
import com.zhai.shuyangwx.greendao.BookDataDao;
import com.zhai.shuyangwx.greendao.BookReadHistoryDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 历史阅读记录数据库管理类
 */
public class BookReadHistoryDaoUtils {
    private static final String TAG = BookReadHistoryDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public BookReadHistoryDaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成BookshelfBean记录的插入，如果表未创建，先创建MBookshelfBean表
     * @param bookReadHistory
     * @return
     */
    public boolean insertBookReadHistory(BookReadHistory bookReadHistory){
        boolean flag = false;
        flag = mManager.getDaoSession().getBookReadHistoryDao().insert(bookReadHistory) == -1 ? false : true;
        Log.i(TAG, "insert Meizi :" + flag + "-->" + bookReadHistory.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param bookReadHistories
     * @return
     */
    public boolean insertMultBookReadHistory(final List<BookReadHistory> bookReadHistories) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (BookReadHistory meizi : bookReadHistories) {
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
     * @param bookReadHistory
     * @return
     */
    public boolean updateBookReadHistory(BookReadHistory bookReadHistory){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(bookReadHistory);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param bookReadHistory
     * @return
     */
    public boolean deleteBookReadHistory(BookReadHistory bookReadHistory){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(bookReadHistory);
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
            mManager.getDaoSession().deleteAll(BookReadHistory.class);
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
    public List<BookReadHistory> queryAllBookReadHistory(){
        return mManager.getDaoSession().loadAll(BookReadHistory.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public BookReadHistory queryBookReadHistoryById(long key){
        return mManager.getDaoSession().load(BookReadHistory.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<BookReadHistory> queryBookReadHistoryByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(BookReadHistory.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<BookReadHistory> queryBookReadHistoryQueryBuilder(String id){
        QueryBuilder<BookReadHistory> queryBuilder = mManager.getDaoSession().queryBuilder(BookReadHistory.class);
        return queryBuilder.where(BookReadHistoryDao.Properties.Bookid.eq(id)).list();
    }
    /**
     * 关闭数据库
     */
    public void closeConnection(){
        mManager.closeConnection();
    }
}
