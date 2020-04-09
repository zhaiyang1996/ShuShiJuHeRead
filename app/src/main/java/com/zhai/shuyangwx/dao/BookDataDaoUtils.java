package com.zhai.shuyangwx.dao;

import android.content.Context;
import android.util.Log;

import com.zhai.shuyangwx.bean.BookData;
import com.zhai.shuyangwx.bean.BookshelfBean;
import com.zhai.shuyangwx.greendao.BookDataDao;
import com.zhai.shuyangwx.greendao.BookshelfBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class BookDataDaoUtils {
    private static final String TAG = BookDataDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public BookDataDaoUtils(Context context){
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成BookshelfBean记录的插入，如果表未创建，先创建MBookshelfBean表
     * @param bookData
     * @return
     */
    public boolean insertBookData(BookData bookData){
        boolean flag = false;
        flag = mManager.getDaoSession().getBookDataDao().insert(bookData) == -1 ? false : true;
        Log.i(TAG, "书籍下载成功 :" + flag + "-->" + bookData.getTitle());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param bookDataList
     * @return
     */
    public boolean insertMultBookshelfBean(final List<BookData> bookDataList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (BookData meizi : bookDataList) {
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
     * @param bookData
     * @return
     */
    public boolean updateBookData(BookData bookData){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(bookData);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param bookData
     * @return
     */
    public boolean deleteBookData(BookData bookData){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(bookData);
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
            mManager.getDaoSession().deleteAll(BookData.class);
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
    public List<BookData> queryAllBookData(){
        return mManager.getDaoSession().loadAll(BookData.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public BookData queryBookDataById(long key){
        return mManager.getDaoSession().load(BookData.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<BookData> queryBookDataByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(BookData.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<BookData> queryBookDataDaoByQueryBuilder(String id,String titble){
        QueryBuilder<BookData> queryBuilder = mManager.getDaoSession().queryBuilder(BookData.class);
        return queryBuilder.where(BookDataDao.Properties.BookId.eq(id),BookDataDao.Properties.Title.eq(titble)).list();
    }
    /**
     * 关闭数据库
     */
    public void closeConnection(){
        mManager.closeConnection();
    }
}
