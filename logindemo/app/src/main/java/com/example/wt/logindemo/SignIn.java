package com.example.wt.logindemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class SignIn extends Activity {
    private EditText mUserName, mPassword, mEmail;
    private Button mBtnSignIn;
    private TextView mTvTips, mTvToLogin;
    private android.os.Handler mHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                AVObject userInfo = new AVObject("USERINFO");
                userInfo.put("username", mUserName.getText().toString().trim());
                userInfo.put("password", mPassword.getText().toString().trim());
                userInfo.put("email", mEmail.getText().toString().trim());
                userInfo.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.d("成功", "注册");
                            Intent it = new Intent(SignIn.this, MyActivity.class);
                            startActivity(it);

                        } else {
                            mTvTips.setText("注册失败，请重试。");
                        }

                    }
                });
            }else if(msg.what == 1){
                mTvTips.setText("邮箱已注册");
            }else {
                mTvTips.setText("网络异常");
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.siginin);
        initView();
    }

    private void initView( ){
        mUserName = (EditText)findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mEmail = (EditText)findViewById(R.id.email);
        mBtnSignIn = (Button)findViewById(R.id.signIn);
        mTvTips = (TextView)findViewById(R.id.forgePassword);
        mTvToLogin = (TextView)findViewById(R.id.toLogin);

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserName.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                System.out.println("username:"+username+"password:"+password+"email:"+email);
                if (isNull(username)){
                    mTvTips.setText("用户名不能为空");
                    return ;

                }
                if (isNull(password)){
                    mTvTips.setText("密码名不能为空");

                }
                else if (isNull(email)){
                    mTvTips.setText("邮箱名不能为空");

                }

                else if(isLen(username)){
                    mTvTips.setText("用户名长度需要大于6");

                }
                else if(isLen(password)){
                    mTvTips.setText("密码长度需要大于6");

                }
               else if(!isEmail(email)){
                    mTvTips.setText("请输入正确的邮箱");

                }
               else {
                    AVQuery<AVObject> query = new AVQuery<AVObject>("USERINFO");
                    query.whereEqualTo("email", email);
                    query.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> avObjects, AVException e) {
                            if (e==null){
                                    if (avObjects.size()>1){
                                        Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
                                        mHandler.sendEmptyMessage(1);
                                    }else{
                                        mHandler.sendEmptyMessage(0);
                                    }
                            }else{
                                Log.d("失败", "查询错误: " + e.getMessage());
                                mHandler.sendEmptyMessage(-1);

                            }
                        }
                    });
                }
            }
        });

        mTvToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SignIn.this, MyActivity.class);
                startActivity(it);
            }
        });

    }

    private boolean isNull(String str){
        return "".equals(str);
    }

    private boolean isLen(String str){
        return str.length()<6;

    }

    private boolean isEmail(String str){
        return str.contains("@");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_in, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
