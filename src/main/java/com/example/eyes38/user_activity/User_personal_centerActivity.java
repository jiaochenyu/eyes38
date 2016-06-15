package com.example.eyes38.user_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eyes38.R;
import com.example.eyes38.utils.GetToken;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class User_personal_centerActivity extends AppCompatActivity {
    public static final int mWHAT = 321;
    public static final int MFINISH = 322;
    public static final int MWHAT2 = 323;
    public static final int MFINISH2 = 344;
    //图片上传部分
    private String mUri;
    private String image_uri;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;//拍照
    private static final int PHOTOZOOM = 2;//缩放
    private static final int PHOTORESULT = 3;//缩放
    Dialog mDialog;//头像上传弹框
    //先从偏好设置中取出账号
    private SharedPreferences sp;
    //初始化数据
    private String customer_id, username, firstname, sex, eamil;
    //更新的数据
    private String update_name, update_email, update_sex;
    //定义控件
    private TextView person_center_tel;
    private EditText person_center_name, person_center_email;
    private Spinner mSpinner;
    private RequestQueue mRequestQueue;
    private Button user_person_center_save;
    Intent mIntent;
    //七牛云
    public static final String TAG = "MyText";
    String netPath = "http://o8oqvjhsv.bkt.clouddn.com";//外链域名
    String name = "caojunzzia";//我要上传的空间名
    private ImageView image_button;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MFINISH:
                    break;
                case MFINISH2:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_personal_center);
        //初始化控件
        initViews();
        //先将bean中的数据传入
        initData();
        //在更新数据
        updateData();
    }

    //更新数据
    private void updateData() {
        initImageButton();//头像更新按钮监听
        initEditListener();
    }

    private void initImageButton() {
        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUploadPhoto();
            }
        });

    }

    private void initEditListener() {

        //对确定按钮进行监听
        user_person_center_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //姓名文本改变之后
                update_name = person_center_name.getText().toString();
                sex = mSpinner.getSelectedItem().toString();
                if (sex.equals("男")) {
                    update_sex = "M";
                } else {
                    update_sex = "F";
                }
                update_email = person_center_email.getText().toString();
                httpMethod2();
                finish();
            }
        });


    }

    //进行数据的更新更新
    private void httpMethod2() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/" + customer_id;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("email", update_email);
        if (mUri != null) {
            request.add("image", mUri + "");
        }
        request.add("firstname", update_name);
        request.add("sex", update_sex);
        mRequestQueue.add(MWHAT2, request, mOnResponseListener);
    }

    //初始化控件上需要显示的值
    private void initData() {
        mRequestQueue = NoHttp.newRequestQueue();
        //先从偏好设置中取出customer_id
        customer_id = sp.getString("CUSTOMER_ID", "");
        httpMethod();
    }

    private void httpMethod() {
        String url = "http://38eye.test.ilexnet.com/api/mobile/customer-api/customers/" + customer_id;
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        mRequestQueue.add(mWHAT, request, mOnResponseListener);
    }

    private OnResponseListener<String> mOnResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == mWHAT) {
                //请求成功
                String result = response.get();
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject object1 = object.getJSONObject("data");
                    username = object1.getString("username");
                    firstname = object1.getString("firstname");
                    sex = object1.getString("sex");
                    eamil = object1.getString("email");
                    image_uri = object1.getString("image");
                    Glide.with(User_personal_centerActivity.this)
                            .load(image_uri)
                            .bitmapTransform(new CropCircleTransformation(User_personal_centerActivity.this))
                            .error(R.mipmap.user_photo)
                            .into(image_button);
                    //用户名
                    person_center_tel.setText(username);
                    person_center_tel.setTextSize(15);
                    //姓名
                    person_center_name.setText(firstname);
                    person_center_tel.setTextSize(15);
                    //性别
                    if (sex.equals("M")) {
                        mSpinner.setSelection(1);

                    } else {
                        mSpinner.setSelection(0);
                    }
                    mSpinner.setVisibility(View.VISIBLE);
                    //邮箱
                    person_center_email.setText(eamil);
                    person_center_tel.setTextSize(15);
                    Message message = new Message();
                    message.what = MFINISH;
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == MWHAT2) {
                Message message = new Message();
                message.what = MFINISH2;
                mHandler.sendMessage(message);
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        }

        @Override
        public void onFinish(int what) {

        }
    };

    //完成控件的绑定
    private void initViews() {
        //绑定控件
        user_person_center_save = (Button) findViewById(R.id.person_center_save);
        person_center_tel = (TextView) findViewById(R.id.person_center_tel);
        person_center_name = (EditText) findViewById(R.id.person_center_name);
        image_button = (ImageView) findViewById(R.id.user_person_photo);
        mSpinner = (Spinner) findViewById(R.id.spinner_sex);
        person_center_email = (EditText) findViewById(R.id.person_center_email);
        //初始化偏好设置
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);//偏好设置
    }

    //返回键
    public void user_personal_center_back(View view) {
        finish();
    }

    //上传头像操作
    private void doUploadPhoto() {
        View view = getLayoutInflater().inflate(R.layout.my_upload_photo, null);//绑定布局
        Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //获取对话框的window实例
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        mDialog = dialog;
    }

    //本地上传图片
    public void local_upload(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTOZOOM);
        mDialog.dismiss();
    }

    //拍照上传图片
    public void camera_upload(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "a.jpg")));
        startActivityForResult(intent, PHOTOHRAPH);
        mDialog.dismiss();
    }

    //取消上传
    public void cancel_upload(View view) {
        mDialog.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NONE) {
            return;
        }
        //拍照
        if (requestCode == PHOTOHRAPH) {
            //设置文件保存路径(暂时放在根目录下)
            File pic = new File(Environment.getExternalStorageDirectory() + "/a.jpg");
            startPhotoZoom(Uri.fromFile(pic));
        }
        if (data == null) {
            return;
        }
        //读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
            startPhotoZoom(data.getData());
        }
        //处理结果
        if (requestCode == PHOTORESULT) {
            //返回数据
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = bundle.getParcelable("data");
                image_button.setImageBitmap(bitmap);
                //获得imageView中设置的图片
                BitmapDrawable drawable = (BitmapDrawable) image_button.getDrawable();
                final Bitmap bmp = drawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imgbyte = baos.toByteArray();
                GetToken mGetToken;
                final String token;
                //imageName为上传空间中的名称，为了确保唯一，使用（id+系统当前时间）命名
                final String imgname = customer_id + System.currentTimeMillis() + ".jpg";
                final String headImagePath = "http://o8oqvjhsv.bkt.clouddn.com/" + imgname;
                mUri = headImagePath;
                if (imgbyte != null) {
                    mGetToken = new GetToken();
                    token = mGetToken.getToken(name);
                    UploadManager uploadManager = new UploadManager();
                    uploadManager.put(imgbyte, imgname, token,
                            new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                    if (info.statusCode == 200) {
                                        Toast.makeText(getApplication(), "完成上传", Toast.LENGTH_LONG).show();
                                        Glide.with(User_personal_centerActivity.this)
                                                .load(headImagePath)
                                                .bitmapTransform(new CropCircleTransformation(User_personal_centerActivity.this))
                                                .error(R.mipmap.user_photo)
                                                .into(image_button);

                                    } else {
                                        Toast.makeText(getApplication(), info.statusCode + "上传失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, null);

                }

            }
        }
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        //设置传递的宽和高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 120);
        intent.putExtra("outputY", 120);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESULT);
    }
}
