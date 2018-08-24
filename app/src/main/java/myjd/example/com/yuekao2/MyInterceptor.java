package myjd.example.com.yuekao2;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl httpUrl = request.url()
                .newBuilder()
                .addQueryParameter("source", "android")
                .build();
        Request build = request.newBuilder()
                .url(httpUrl)
                .build();
        Log.i("aaa",request.toString());
        return chain.proceed(build);
    }

}
