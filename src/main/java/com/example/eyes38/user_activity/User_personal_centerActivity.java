package com.example.eyes38.user_activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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

import com.example.eyes38.R;
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

public class User_personal_centerActivity extends AppCompatActivity {
    public static final int mWHAT = 321;
    public static final int MFINISH = 322;
    public static final int MWHAT2 = 323;
    public static final int MFINISH2 = 344;
    Context mContext;
    private ContentResolver mContentResolver;
    //图片上传部分
    private Uri mUri;
    private String image_uri;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    public static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;//拍照
    private static final int PHOTOZOOM = 2;//缩放
    private static final int PHOTORESULT = 3;//缩放
    Dialog mDialog;//头像上传弹框
    private Bitmap resultBmp;
    //先从偏好设置中取出账号
    private SharedPreferences sp;
    //初始化数据
    private String customer_id, username, firstname, sex, eamil;
    //更新的数据
    private String update_name, update_email, update_sex, update_image;
    //定义控件
    private TextView person_center_tel;
    private EditText person_center_name, person_center_email;
    private Spinner mSpinner;
    private ImageView image_button;
    private RequestQueue mRequestQueue;
    private Button user_person_center_save;
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
        Log.e("image上传的uri", mUri + "");
        request.add("image", mUri + "");
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
                    Log.e("image下拉的uri", image_uri);
                    if (image_uri.equals("")) {
                        image_button.setImageResource(R.mipmap.user_photo);
                        Log.e("TAGm", "1");
                    } else if (!image_uri.equals("")){
<<<<<<< HEAD
                        //如何通过uri找到本地图片呢,草 ，不开心  真心不会
                        image_button.setImageResource(R.mipmap.user_photo);
                      /*  try {
                           String uri= GetPathFromUri4kitkat.getImageAbsolutePath(User_personal_centerActivity.this, Uri.parse(image_uri));
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContentResolver, Uri.parse(uri));
                            image_button.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e("TAGm", "2");*/
=======
                        image_button.setImageResource(R.drawable.user_photo);

>>>>>>> 8e24ee704d7712d433fb39ed0fe4c1d9c1d1c139
                    }
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
        mContentResolver=getContentResolver();
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
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = bundle.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                image_button.setImageBitmap(bitmap);
                //获得imageView中设置的图片
                BitmapDrawable drawable = (BitmapDrawable) image_button.getDrawable();
                Bitmap bmp = drawable.getBitmap();
                //获得图片的宽，并创建结果bitmap
                int width = bmp.getWidth();
                resultBmp = Bitmap.createBitmap(width, width,
                        Bitmap.Config.ARGB_8888);//该函数创建一个带有特定宽度、高度和颜色格式的位图。
                Paint paint = new Paint();//创建新画笔
                Canvas canvas = new Canvas(resultBmp);//为该位图创建一个画布
                //去锯齿
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setDither(true);
                //画圆
                canvas.drawCircle(width / 2, width / 2, width / 2, paint);//圆心坐标  半径 和画笔
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 选择交集去上层图片
                canvas.drawBitmap(bmp, 0, 0, paint);//将位图画到指定坐标
                image_button.setImageBitmap(resultBmp);
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
        mUri = uri;
        Log.e("image_uri", mUri + "");
    }


}
