package myjd.example.com.yuekao2;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import myjd.example.com.yuekao2.bean.UploadBean;

public class MainActivity extends AppCompatActivity implements MainView.View {


    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.sdv)
    ImageView sdv;
    private PopupWindow pop;
    private MainPresenter presenter;
    private File file1;
    private String uid="71";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},200);
        }
        presenter = new MainPresenter(this);
        sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop = new PopupWindow(MainActivity.this);
                View view = View.inflate(MainActivity.this,R.layout.pop,null);
                pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                //只要不设置为空，点击外部可返回
                pop.setBackgroundDrawable(new BitmapDrawable());
                ///这里必须设置为true才能点击区域外或者消失
                pop.setFocusable(true);
                //这个控制PopupWindow内部控件的点击事件
                pop.setOutsideTouchable(true);
                pop.setContentView(view);
                Button bt1 = (Button) view.findViewById(R.id.pop_paizhao);
                Button bt2 = (Button) view.findViewById(R.id.pop_xiangce);
                Button bt3 = (Button) view.findViewById(R.id.pop_cancle);
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent picture = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(picture, 1);
                        pop.dismiss();
                    }
                });
                bt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();
            file1 = getFileByUri(uri, MainActivity.this);
            Log.i("1TAG", "onActivityResult: "+file1);
            presenter.setData();
            ContentResolver resolver = getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                sdv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private File getFileByUri(Uri uri, MainActivity mainActivity) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else {
        }
        return null;
    }


    @Override
    public void onSuccessful(UploadBean uploadBean) {
        Log.i("TAG", "onSuccessful: "+uploadBean.getMsg());
    }

    @Override
    public void onError(String error) {
        Log.i("TAG", "onError: "+error);
    }

    @Override
    public File setFile() {
        return file1;
    }

    @Override
    public String setUid() {
        return uid;
    }
}
