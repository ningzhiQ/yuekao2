package myjd.example.com.yuekao2;

import myjd.example.com.yuekao2.bean.UploadBean;

public class MainPresenter implements MainView.Presenter,MainView.GetDataState{
    private MainView.View mainView;
    private MainView.Model model;

    public MainPresenter(MainView.View mainView) {
        this.mainView = mainView;
        this.model = new MainModel();
    }

    @Override
    public void onSuccessful(UploadBean uploadBean) {
        mainView.onSuccessful(uploadBean);
    }

    @Override
    public void onError(String error) {
        mainView.onError(error);
    }

    @Override
    public void setData() {
        model.setData(mainView.setUid(),mainView.setFile(),this);
    }

    @Override
    public void onDestory() {
        if (mainView!=null) {
            mainView=null;
        }
    }
}
