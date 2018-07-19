package com.shushijuhe.shushijuheread.http;

import com.shushijuhe.shushijuheread.bean.Author_booksBean;
import com.shushijuhe.shushijuheread.bean.AutoComplete;
import com.shushijuhe.shushijuheread.bean.BookDetailBean;
import com.shushijuhe.shushijuheread.bean.BookMixAToc;
import com.shushijuhe.shushijuheread.bean.Book_infoBean;
import com.shushijuhe.shushijuheread.bean.CategoriesBean;
import com.shushijuhe.shushijuheread.bean.Categories_infoBean;
import com.shushijuhe.shushijuheread.bean.ChapterRead;
import com.shushijuhe.shushijuheread.bean.RankBean;
import com.shushijuhe.shushijuheread.bean.Rank_categoryBean;
import com.shushijuhe.shushijuheread.bean.Sub_CategoriesBean;
import com.shushijuhe.shushijuheread.bean.UpAppBean;
import com.shushijuhe.shushijuheread.bean.VideoSeekBean;

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


    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
