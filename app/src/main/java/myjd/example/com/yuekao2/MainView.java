package myjd.example.com.yuekao2;

import java.io.File;

import myjd.example.com.yuekao2.bean.UploadBean;

public interface MainView {
    interface Model {
        void setData(String uid, File file, GetDataState callBack);
    }

    interface GetDataState {
        void onSuccessful(UploadBean uploadBean);
        void onError(String error);
    }
    interface View extends GetDataState {
        @Override
        void onSuccessful(UploadBean uploadBean);

        @Override
        void onError(String error);

        File setFile();
        String setUid();
    }

    interface Presenter {
        void setData();
        void onDestory();
    }
}
