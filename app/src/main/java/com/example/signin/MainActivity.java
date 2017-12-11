package com.example.signin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.util.InetAddressUtils;

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
                String username=name.getText().toString();
                String userpass=password.getText().toString();
                if(TextUtils.isEmpty(username.trim())||TextUtils.isEmpty(userpass.trim())){
                    Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();

                }else{
                    loginSendData(username,userpass);
                }
            }
        });

    }


    //网址：192.168.136.122:8080/MySign/login

    private void loginSendData(String name,String password){
        AsyncHttpClient asyncHttpClient= new AsyncHttpClient();
        String url="120.78.76.219/CP/servlet/login";
        RequestParams params=new RequestParams();
        params.put("username",name);
        params.put("userpass",password);
        asyncHttpClient.post(url.toString().trim(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    sendIP(getLocalIpAddress(),getCurrentTime());

                    /*textView.setText(new String(responseBody));
                    Toast.makeText(getApplicationContext(),new String(responseBody),Toast.LENGTH_LONG).show();*/
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //打印异常信息
                error.printStackTrace();
            }
        });
    }

    //获取本地IP
    public  String getLocalIpAddress() {
        try {
            String ipv4;

            List nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (int i = 0;i < nilist.size(); i++)
            {
                NetworkInterface ni = (NetworkInterface) nilist.get(i);
                List  ialist = Collections.list(ni.getInetAddresses());
                for (int j = 0; j < ialist.size(); j++){
                    InetAddress address = (InetAddress) ialist.get(j);
                    if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4=address.getHostAddress()))
                    {
                        return ipv4;
                    }
                }

            }

        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private void sendIP(String myIP,String str){
        AsyncHttpClient asyncHttpClient= new AsyncHttpClient();
        String url="120.78.76.219/CP/servlet/receive";
        RequestParams params=new RequestParams();
        params.put("ip",myIP);
        params.put("time",str);
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(),"获取IP成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private String getCurrentTime(){

        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH时mm分ss秒");
        Date curDate = new Date();//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }



}
