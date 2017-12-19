package com.example.signin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import JsonBean.JsonUtils;
import JsonBean.Response;
import cz.msebera.android.httpclient.Header;

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
        String url="http://192.168.136.122:8080/Struts2Sign/login.action";
        RequestParams params=new RequestParams();
        params.put("username",name);
        params.put("userpass",password);
        asyncHttpClient.post(url, params, new BaseJsonHttpResponseHandler<Response>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Response response) {
                if (statusCode==200){
                    //获取Id
                    Response mresponse = JsonUtils.getResponse(rawJsonResponse);
                    final String id = mresponse.getId();
                    Log.d("获得的id______________",id);
                    Intent intent= new Intent(MainActivity.this,Second.class);
                    Bundle bundle= new Bundle();
                    bundle.putString("id",id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    /*textView.setText(new String(responseBody));
                    Toast.makeText(getApplicationContext(),new String(responseBody),Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Response errorResponse) {

            }

            @Override
            protected Response parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

    }
}
