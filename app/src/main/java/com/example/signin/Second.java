package com.example.signin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;

import JsonBean.Response;
import cz.msebera.android.httpclient.Header;


public class Second extends AppCompatActivity {

    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btn_logout=(Button)findViewById(R.id.btn_logout);
        Bundle bundle= getIntent().getExtras();
        final String id=bundle.getString("id");
        sendTimeIp(getLocalIpAddress(),getCurrentTime(),id);
        Log.d("sendipfirst","_________________________________sendfirstsuccess");

        //定时任务实现
        final Handler handler= new Handler();
        final Runnable runnable= new Runnable() {
            @Override
            public void run() {
                sendTimeIp(getLocalIpAddress(),getCurrentTime(),id);
                handler.postDelayed(this,15*1000);
            }
        };
        handler.postDelayed(runnable,15*1000);
       /* //新建一个定时器，用于定时发送IP和时间
        Timer timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendTimeIp(getLocalIpAddress(),getCurrentTime(),id);
            }
        },30*1000,30*1000);*/



        //定时器关闭的，当网络断开时handler.removeCallbacks(runnable);
        if (!isWifiEnabled(getApplicationContext())){
            handler.removeCallbacks(runnable);
            Toast.makeText(getApplicationContext(),"网络已断开",Toast.LENGTH_LONG).show();
        }

        //退出登录
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                Toast.makeText(getApplicationContext(),"退出登录成功",Toast.LENGTH_LONG).show();
                Intent intent= new Intent();
                intent.setClass(Second.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //或者是Intent.FLAG_ACTIVITY_CLEAR_TASK清除所有，
                // 而FLAG_ACTIVITY_CLEAR_TOP是本Activity存在过才destory
                startActivity(intent);
            }
        });
    }



    //发送时间，IP，id
    public void sendTimeIp(String myIP,String str,String id){
        AsyncHttpClient asyncHttpClient= new AsyncHttpClient();
        String url="http://192.168.136.122:8080/Struts2Sign/receive.action";
        RequestParams params=new RequestParams();
        params.put("ip",myIP);
        params.put("time",str);
        params.put("id",id);
        asyncHttpClient.post(url, params, new BaseJsonHttpResponseHandler<Response>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Response response) {
                if(statusCode==200){
                    //解析数据，后面用get方法即可提取数据
                    //Response mresponse = JsonUtils.getResponse(rawJsonResponse);
                    //Log.d("SecondActivity","___________________________SuccessofSendIp"+mresponse);
                    Toast.makeText(getApplicationContext(),"获取IP成功",Toast.LENGTH_LONG).show();
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

    //获取本地IP
    public  String getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ip = Formatter.formatIpAddress(info.getIpAddress());
        Log.d("ip","________________________"+ip);
        return ip;

        /*try {
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
                        Log.d("ip","__________________________________________"+ipv4);
                        return ipv4;
                    }
                }

            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;*/
    }

    //获取系统的时间
    public String getCurrentTime(){
        long time = System.currentTimeMillis();
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= new Date(time);
        String str=format.format(date);
        Log.d("getTime","___________________________"+str);
        return str;
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
