package myjd.example.com.yuekao2;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUntils {
    private static volatile RetrofitUntils instance;
    private Retrofit retrofit;

    private RetrofitUntils(){

    }
    private RetrofitUntils(String baseUrl){
        OkHttpClient client = new OkHttpClient();
        retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }
    public static RetrofitUntils getInstance(String baseUrl){
        if (instance==null){
            synchronized (RetrofitUntils.class){
                if (null==instance){
                    instance = new RetrofitUntils(baseUrl);
                    //拦截器
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new MyInterceptor())
                            .build();
                }
            }
        }
        return instance;

    }
    public static RetrofitUntils getInstance(){
        if (null == instance){
            return  getInstance("https://www.zhaoapi.cn/");
        }
        return instance;
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
}
