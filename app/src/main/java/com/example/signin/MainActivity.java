package com.example.signin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.util.InetAddressUtils;

import static com.example.signin.Second.isWifiEnabled;


public class MainActivity extends AppCompatActivity {

    private EditText name,password;
    private Button btn_in;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //找到控件
        name=(EditText)findViewById(R.id.edit_name);
        password=(EditText)findViewById(R.id.edit_pass);
        textView=(TextView)findViewById(R.id.tv_result);

        btn_in=(Button)findViewById(R.id.btn_denglu);



        //设置登录的监听事件
        btn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断手机是否处于联网状态弹出消息提醒
                if (!isWifiEnabled(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"请连接网络！",Toast.LENGTH_LONG).show();
                }

                String username=name.getText().toString();
                String userpass=password.getText().toString();
                if(TextUtils.isEmpty(username.trim())||TextUtils.isEmpty(userpass.trim())){
                    Toast.makeText(getApplicationContext(),"账号或密码不能为空！",Toast.LENGTH_SHORT).show();

                }else{
                    loginSendData(username,userpass);
                }
            }
        });

    }


    //网址：192.168.136.122:8080/MySign/login
    //登录连接服务器
    private void loginSendData(String name,String password){
        AsyncHttpClient asyncHttpClient= new AsyncHttpClient();
        String url="http://120.78.76.219/CP/servlet/login";
        RequestParams params=new RequestParams();
        params.put("username",name);
        params.put("userpass",password);
        asyncHttpClient.post(url.toString().trim(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //打印异常信息
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (statusCode==200){
                    //获取Id
                    final String id = responseString;
                    Log.d("获得的返回值______________",id);


                    Intent intent= new Intent(MainActivity.this,Second.class);
                    Bundle bundle= new Bundle();
                    bundle.putString("id",id);
                    intent.putExtra("bundle",bundle);

                    startActivity(intent);



                    /*textView.setText(new String(responseBody));
                    Toast.makeText(getApplicationContext(),new String(responseBody),Toast.LENGTH_LONG).show();*/
                }
            }
        });
    }





}
