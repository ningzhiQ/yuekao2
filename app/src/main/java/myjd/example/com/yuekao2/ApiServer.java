package myjd.example.com.yuekao2;

import myjd.example.com.yuekao2.bean.UploadBean;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiServer {
    @Multipart
    @POST("file/upload")
    Observable<UploadBean> rxUpload (@Query("uid")String uid, @Part MultipartBody.Part file);
}
