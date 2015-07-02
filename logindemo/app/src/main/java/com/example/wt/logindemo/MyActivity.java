package com.example.wt.logindemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;


public class MyActivity extends Activity {

    private EditText mUserName, mPassword;
    private Button mBtnLogin, mBtnSignIn;
    private TextView mTvForgetPassword;
    private MyClickListener mMyClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initView();

    }

    private void initView(){
        mUserName = (EditText)findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mBtnLogin = (Button)findViewById(R.id.login);
        mBtnSignIn = (Button)findViewById(R.id.signIn);
        mTvForgetPassword = (TextView)findViewById(R.id.forgePassword);
        mTvForgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvForgetPassword.getPaint().setAntiAlias(true);

        mMyClickListener = new MyClickListener();
        mBtnLogin.setOnClickListener(mMyClickListener);
        mBtnSignIn.setOnClickListener(mMyClickListener);
        mTvForgetPassword.setOnClickListener(mMyClickListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signIn:
                    Intent it = new Intent(MyActivity.this, SignIn.class);
                    startActivity(it);
                    break;
                case R.id.login:
                    String username = mUserName.getText().toString().trim();
                    final String password = mPassword.getText().toString().trim();
                    if (username.equals("")){
                        Toast.makeText(MyActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    }else if (password.equals("")){
                        Toast.makeText(MyActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                    AVQuery<AVObject> query = new AVQuery<AVObject>("USERINFO");
                    query.whereEqualTo("username", username);
                    query.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> avObjects, AVException e) {
                            Log.d("wt",avObjects.toString());
                            if (e==null){
                                if(avObjects.get(0).getString("password").toString().trim().equals(password) ){
                                    Toast.makeText(MyActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MyActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Log.d("失败", "查询错误: " + e.getMessage());
                                Toast.makeText(MyActivity.this,"登录失败，请重试",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    }
                    break;
                case R.id.forgePassword:
                    Intent it1 = new Intent(MyActivity.this, MyWeb.class);
                    startActivity(it1);
                    break;
            }
        }
    }
}
