package myjd.example.com.yuekao2;

import java.io.File;

import myjd.example.com.yuekao2.bean.UploadBean;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainModel implements MainView.Model{
    @Override
    public void setData(String uid, File file, final MainView.GetDataState callBack) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RetrofitUntils.getInstance().getRetrofit().create(ApiServer.class)
                .rxUpload(uid,body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UploadBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(UploadBean uploadBean) {
                        callBack.onSuccessful(uploadBean);
                    }
                });

    }
}
