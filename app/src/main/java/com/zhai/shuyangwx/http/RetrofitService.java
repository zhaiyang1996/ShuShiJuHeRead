package com.zhai.shuyangwx.http;


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
import com.zhai.shuyangwx.bean.VideoSeekBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zhaiyang on 2018/5/9.
 */

public interface RetrofitService {

    /**
     * 获取书籍的详细内容
     * @param bookId :书籍id
     * @return
     */
    @GET("/book/{bookId}")
    Observable<BookDetailBean> getBookDetail(@Path("bookId") String bookId);
    /**
     * 获取带书籍数量的父分类
     */
    @GET("/cats/lv2/statistics")
    Observable<CategoriesBean> getCategories();

    /**
     * 获取父分类下的子分类
     */
    @GET("/cats/lv2")
    Observable<Sub_CategoriesBean> getSub_Categories();

    /**
     * 获取分类详情
     * @param gender male、female
     * @param type   hot(热门)、new(新书)、reputation(好评)、over(完结)
     * @param major  玄幻
     * @param minor  东方玄幻、异界大陆、异界争霸、远古神话
     * @param start  0 (页码)
     * @param limit  20
     * @return
     */
    @GET("/book/by-categories")
    Observable<Categories_infoBean> getCategories_info(@Query("gender") String gender,
                                                       @Query("type") String type,
                                                       @Query("major") String major,
                                                       @Query("minor") String minor,
                                                       @Query("start") String start,
                                                       @Query("limit") String limit);

    /**
     *获取搜索结果
     * query:书籍关键字（作者、书名等）
     */
    @GET("/book/fuzzy-search")
    Observable<Book_infoBean> getBook_info(@Query("query") String query);
    /**
     * 获取作者名下的书籍
     *author:作者名
     */
    @GET("/book/accurate-search")
    Observable<Author_booksBean> getAuthor_books(@Query("author") String author);

    /**
     * 获取书籍章节
     * bookId: {书籍id}
     * view :chapters (固定值)
     */
    @GET("/mix-atoc/{bookId}")
    Observable<BookMixAToc> getBookMixAToc(@Path("bookId") String bookId, @Query("view") String view);

    /**
     * 关键字自动补全
     *
     * @param query
     * @return
     */
    @GET("/book/auto-complete")
    Observable<AutoComplete> autoComplete(@Query("query") String query);


    /**
     * 获取排名分类
     */
    @GET("/ranking/gender")
    Observable<Rank_categoryBean> getRank_category();

    /**
     * 获取排名详情
     * id:排名分类的id
     */
    @GET("/ranking/{rankingId}")
    Observable<RankBean> getRank(@Path("rankingId") String rankingId);

    /**
     * 获取书籍详细类容
     * @param :书籍源like
     * @return
     */
    @GET("http://chapter2.zhuishushenqi.com/chapter/{url}")
    Observable<ChapterRead> getChapterRead(@Path("url") String url);

    @GET("http://47.97.207.187/json.php")
    Observable<List<VideoSeekBean>> getVideoBean(@Query("api") String api,
                                                 @Query("key") String key);

    /**
     * @return 获取留言板内容
     */
    @POST("http://yangzimeng.vip:8080/VodJson/servlet/getMessageJson")
    Observable<MsgBean> getMsg();

    /**
     * 获取传送门地址
     */
    @POST("http://yangzimeng.vip:8080/isGHS.json")
    Observable<GHSBean> getISGHS();

    /**
     * 获取卡密时限
     */
    @POST
    Observable<KMBean> getKM(@Url() String url);
    /**
     * 获取XX视频分类
     */
    @POST
    Observable<VodClassBean> getXXClassJson(@Url() String url);
    /**
     * 获取XX视频分类详情
     */
    @POST
    Observable<VideoSeekBean> getXXSeekJson(@Url() String url);

    /**
     * 获取点播列表
     */
    @POST("http://yangzimeng.vip:8080/VodJson/servlet/getDBT")
    Observable<DBTBean> getDBT(@Query("room_id") String id);
    /**
     * 添加点播
     */
    @POST("http://yangzimeng.vip:8080/VodJson/servlet/setDBT_vod")
    Observable<StateBean> setDBT(@Query("room_id") String id,
                                 @Query("name") String name,
                                 @Query("vod_url") String vod_url,
                                 @Query("vod_name") String vod_name,
                                 @Query("vod_time") String vod_time);
}