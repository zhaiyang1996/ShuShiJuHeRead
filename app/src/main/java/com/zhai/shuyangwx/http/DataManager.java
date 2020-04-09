package com.zhai.shuyangwx.http;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.zhai.shuyangwx.activity.base.VodClassBean;
import com.zhai.shuyangwx.bean.Author_booksBean;
import com.zhai.shuyangwx.bean.AutoComplete;
import com.zhai.shuyangwx.bean.BookDetailBean;
import com.zhai.shuyangwx.bean.BookMixAToc;
import com.zhai.shuyangwx.bean.Book_infoBean;
import com.zhai.shuyangwx.bean.CategoriesBean;
import com.zhai.shuyangwx.bean.Categories_infoBean;
import com.zhai.shuyangwx.bean.ChapterRead;
import com.zhai.shuyangwx.bean.DBTBean;
import com.zhai.shuyangwx.bean.GHSBean;
import com.zhai.shuyangwx.bean.KMBean;
import com.zhai.shuyangwx.bean.MsgBean;
import com.zhai.shuyangwx.bean.RankBean;
import com.zhai.shuyangwx.bean.Rank_categoryBean;
import com.zhai.shuyangwx.bean.StateBean;
import com.zhai.shuyangwx.bean.Sub_CategoriesBean;
import com.zhai.shuyangwx.bean.UpAppBean;
import com.zhai.shuyangwx.bean.VideoSeekBean;
import com.zhai.shuyangwx.constants.Constants;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhaiyang on 2018/5/9.
 */

public class DataManager {
    private RetrofitService mRetrofitService;
    private static  DataManager manager;
    private DataManager(){
        this.mRetrofitService = RetrofitHelper.getInstance().getServer();
    }
    public static  DataManager getInstance(){
        if (manager==null){
            manager=new DataManager();
        }
        return manager;
    }

    /**
     * 获取带书籍数量的父分类
     * @param listSubscriber
     */
    public void getCategories(ProgressSubscriber<CategoriesBean> listSubscriber){
        Observable observable=mRetrofitService.getCategories();
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 获取带书籍数量的子分类
     * @param listSubscriber
     */
    public void getSub_CategoriesBean(ProgressSubscriber<Sub_CategoriesBean> listSubscriber){
        Observable observable=mRetrofitService.getSub_Categories();
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 获取分类详情
     * @param listSubscriber
     * @param gender ：male、female
     * @param type ：hot(热门)、new(新书)、reputation(好评)、over(完结)
     * @param major ：主分类
     * @param minor ：子分类
     * @param start ：页码
     * @param limit ：显示条目
     */
    public void getCategories_info(ProgressSubscriber<Categories_infoBean> listSubscriber, String gender, String type,
                                   String major, String minor, String start, String limit){
        Observable observable=mRetrofitService.getCategories_info( gender,  type,  major,  minor,  start,  limit);
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 获取书籍搜索结果
     * @param query :书籍关键字（作者、书名等）
     */
    public void getBook_info(ProgressSubscriber<Book_infoBean> listSubscriber, String query){
        Observable observable=mRetrofitService.getBook_info(query);
        toSubscribe(observable,listSubscriber);
    }


    /**
     *获取作者的所有书籍
     * @param listSubscriber
     * @param author :作者名
     */
    public void getAuthor_books(ProgressSubscriber<Author_booksBean> listSubscriber, String author){
        Observable observable=mRetrofitService.getAuthor_books(author);
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 获取书籍的所有信息
     * @param listSubscriber
     * @param bookid :书籍id
     */
    public void getBookDetail(ProgressSubscriber<BookDetailBean> listSubscriber, String bookid){
        Observable observable=mRetrofitService.getBookDetail(bookid);
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 获取排名分类
     * @param listSubscriber
     */
    public void getRank_category(ProgressSubscriber<Rank_categoryBean> listSubscriber){
        Observable observable=mRetrofitService.getRank_category();
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 搜索文字补全
     * @param listSubscriber
     * @param query
     */
    public void getAutoComplete(ProgressSubscriber<AutoComplete> listSubscriber, String query){
        Observable observable=mRetrofitService.autoComplete(query);
        toSubscribe(observable,listSubscriber);
    }
    /**
     * 获取排名详情
     * @param listSubscriber
     * @param id :排名分类中的id
     */
    public void getRank(ProgressSubscriber<RankBean> listSubscriber, String id){
        Observable observable=mRetrofitService.getRank(id);
        toSubscribe(observable,listSubscriber);
    }

    /**
     *获取书籍目录
     * @param listSubscriber
     * @param bookid 书籍ID
     * @param view 固定值：chapters
     */
    public void getBookMixAToc(ProgressSubscriber<BookMixAToc> listSubscriber, String bookid, String view){
        Observable observable=mRetrofitService.getBookMixAToc(bookid,view);
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 获取书籍具体文章
     * @param listSubscriber
     * @param url ：目录liek
     */
    public void getBookChapter(ProgressSubscriber<ChapterRead> listSubscriber, String url){
        Observable observable=mRetrofitService.getChapterRead(url);
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 获取留言板具体内容
     */
    public void getMsg(ProgressSubscriber<MsgBean> listSubscriber){
        Observable observable=mRetrofitService.getMsg();
        toSubscribe(observable,listSubscriber);
    }
    /**
     * 获取传送门地址
     */
    public void getGHS(ProgressSubscriber<GHSBean> listSubscriber){
        Observable observable=mRetrofitService.getISGHS();
        toSubscribe(observable,listSubscriber);
    }

    /**
     * 获取放映厅点播单
     * @param listSubscriber
     * @param id
     */
    public void getDBT(ProgressSubscriber<DBTBean> listSubscriber,String id){
        Observable observable=mRetrofitService.getDBT(id);
        toSubscribe(observable,listSubscriber);
    }
    /**
     * 添加点播
     */
    public void setDBT(ProgressSubscriber<StateBean> listSubscriber, String id, String name, String vod_url, String vod_name, String vod_time){
        Observable observable=mRetrofitService.setDBT(id,name,vod_url,vod_name,vod_time);
        toSubscribe(observable,listSubscriber);
    }
    /**
     * 获取卡密时限
     * @param listSubscriber
     */
    public void getKM(ProgressSubscriber<KMBean> listSubscriber,String url, String km){
        url = url+"/VodJson/servlet/getKM?km="+km;
        Observable observable=mRetrofitService.getKM(url);
        toSubscribe(observable,listSubscriber);
    }
    /**
     * 获取XX视频分类
     */
    public void getXXClassJson(ProgressSubscriber<VodClassBean> listSubscriber, String url){
        url = url+"/VodJson/servlet/getXXclassJson";
        Observable observable=mRetrofitService.getXXClassJson(url);
        toSubscribe(observable,listSubscriber);
    }

    public void getXXSeekJson(ProgressSubscriber<VideoSeekBean> listSubscriber, String url,String type,String km,String time){
        url = url+"/VodJson/servlet/getXXVodJson?type="+type+"&km="+km+"&time="+time;
        Observable observable=mRetrofitService.getXXSeekJson(url);
        toSubscribe(observable,listSubscriber);
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
