package com.example.signin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.conn.util.InetAddressUtils;
import cz.msebera.android.httpclient.impl.execchain.MainClientExec;


public class Second extends AppCompatActivity {

    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btn_logout=(Button)findViewById(R.id.btn_logout);


        Bundle bundle= getIntent().getExtras();
        final String id=bundle.getString("id");

        //新建一个定时器，用于定时发送IP和时间
        Timer timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //id固定否？不顾规定传不了值，只能传final
                sendTimeIp(getLocalIpAddress(),getCurrentTime(),id);
            }
        },600*1000,600*1000);

        //时间设置是10分钟发送一次
        //sendIP(getLocalIpAddress(),getCurrentTime());

        //定时器关闭的，当网络断开时timer.cancel();
        if (!isWifiEnabled(getApplicationContext())){
            timer.cancel();
            Toast.makeText(getApplicationContext(),"网络已断开",Toast.LENGTH_LONG).show();
        }


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutSendData(id);
            }
        });
    }

    //退出登录
    private void logoutSendData(String id){
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


    //发送时间，IP，id
    public void sendTimeIp(String myIP,String str,String id){
        AsyncHttpClient asyncHttpClient= new AsyncHttpClient();
        String url="http://120.78.76.219/CP/servlet/receive";
        RequestParams params=new RequestParams();
        params.put("ip",myIP);
        params.put("time",str);
        params.put("id",id);
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

    //获取系统的时间
    public String getCurrentTime(){
        Date date= new Date();
        DateFormat formatter =DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
        formatter.format(date);
        return formatter.toString();
    }


    //判断是否连接上WIFI
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }


}
