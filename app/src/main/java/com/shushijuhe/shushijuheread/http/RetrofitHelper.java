package com.shushijuhe.shushijuheread.http;

import com.google.gson.GsonBuilder;
import com.shushijuhe.shushijuheread.constants.Constants;
import com.shushijuhe.shushijuheread.http.handle.ResponseConverterFactory;
import com.shushijuhe.shushijuheread.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 进行Retrofit的初始化工作
 */

public class RetrofitHelper {
    public static final String SERVER_URL= Constants.API_BASE_URL;
    OkHttpClient client ;

    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private boolean debug=true;

    public static RetrofitHelper getInstance(){
        if (instance==null){
            synchronized (Object.class){
                if (instance==null){
                    instance=new RetrofitHelper();
                }
            }
        }
        return instance;
    }
    public OkHttpClient getOkHttpClient() {
        if (client==null){
            client=new OkHttpClient.Builder()
                    .connectTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true) // 失败重发
                    .addInterceptor(getInterceptor())
//                    .adIntderceptor(new ReceivedCookiesInterceptor())
//                    .addInterceptor(new AddCookiesInterceptor())
                    .build();
        }
        return  client;
    }

    private RetrofitHelper(){
        init();
    }

    private void init() {
        resetApp();
    }

    private void resetApp() {
        mRetrofit=new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(getOkHttpClient())
                .addConverterFactory(ResponseConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public RetrofitService getServer(){
        return mRetrofit.create(RetrofitService.class);
    }

    private HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (debug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }

    /**
     * 请求响应日志信息，方便debug
     */
    /*public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Logger.i(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Logger.i(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }*/

    public static class ReceivedCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse =chain.proceed(chain.request());
            //这里获取请求返回的cookie
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                final StringBuffer cookieBuffer = new StringBuffer();
                List<String> header=originalResponse.headers("Set-Cookie");
                if (header.size()==1){
                    for (String s:header){
                        cookieBuffer.append(s.split(";")[0]);
                        break;
                    }
//                    SPUtils.putInt("cookie_count",0);
                    SharedPreferencesUtil.getInstance().putString("cookie",cookieBuffer.toString());
                }else if (header.size()==2){
                    String Cookie = header.get(1).split(";")[0];
//                    Logger.d("当前count值大小:"+count);
//                    if (count>SPUtils.getInt("cookie_count",0)){
//                        cookieBuffer.append(Cookie);
//                        SPUtils.putInt("cookie_count",count);
//                        SPUtils.putString("cookie",cookieBuffer.toString());
//                    }
                    cookieBuffer.append(Cookie);
                    SharedPreferencesUtil.getInstance().putString("cookie",cookieBuffer.toString());
                }
            }
            return originalResponse;
        }
    }

    public static class AddCookiesInterceptor  implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request.Builder builder = chain.request().newBuilder();
//            int cookie_count = SPUtils.getInt("cookie_count", 0);
//            if (cookie_count!=0){
//                builder.addHeader("Count",cookie_count+"");
//            }
            builder.addHeader("Cookie",  SharedPreferencesUtil.getInstance().getString("cookie",""));
            return chain.proceed(builder.build());
        }
    }
}
