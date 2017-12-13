package com.example.signin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.IOException;
import java.net.URI;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.impl.execchain.MainClientExec;

public class Second extends AppCompatActivity {

    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btn_logout=(Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void logoutSendData(int id){
        String url_logout="";
        AsyncHttpClient asyncHttpClient= new AsyncHttpClient();
        RequestParams requestParams= new RequestParams();
        requestParams.put("id",id);
        asyncHttpClient.post(url_logout, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(),"退出登录成功",Toast.LENGTH_LONG).show();
                Intent intent= new Intent();
                intent.setClass(Second.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //或者是Intent.FLAG_ACTIVITY_CLEAR_TASK清除所有，
                // 而FLAG_ACTIVITY_CLEAR_TOP是本Activity存在过才destory
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
