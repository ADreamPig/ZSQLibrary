package com.hongxiang.select;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

/**
 * Created by fjw_zhushunqing on 2017/4/6.
 */

public class LocalService extends Service{

// public static final String HOST = "http://192.168.5.8:8081/api";
    public static final String HOST = "https://api.fangjinnet.com:1000/api";
    public static final String ACTION = "com.hongxiang.select.PollingService";
    private int interval=10;//发送邮件的时间间隔
    private int SuccessCount,ErrorCount;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        request("AppHomeData","",null);
        return super.onStartCommand(intent, flags, startId);
    }






    public void request(String method, String requestData, String token){
        if (!Utils.isNetworkConnected(this)) {
            Intent intent=new Intent(ACTION);

            intent.putExtra("SuccessCount", SuccessCount);
            intent.putExtra("ErrorCount", ErrorCount);
            sendBroadcast(intent);
            return;
        }


        APIData apiData = new APIData();
        if (false) {
//            requestData = TLHttpUtil.e(requestData);
        }
        apiData.setD(requestData);//业务数据JSON格式
        apiData.setM(method);//请求的方法名
        apiData.setV(Utils.getVersionCode(this));//手机客户端版本号
        apiData.setDID(Utils.getDeviceId(this));//手机设备ID
        apiData.setG(Utils.r(this));//请求随机ID
        apiData.setIE(false);//方法是否需要加密
        apiData.setE(1);//加密版本号
        if (token != null) {
            apiData.setT(token);
        }
        apiData.setTS(System.currentTimeMillis());//时间戳，系统时间的秒值,同个应用的不同api请求的time值应该是递增的, 用于防replay攻击
        apiData.setP(1);//1 Android


        JsonRequest<String> jsonRequest=new JsonRequest<String>(Request.Method.POST, HOST, apiData.toString(),new Listener<String>() {

            @Override
            public void onResponse(String s) {
                SuccessCount++;
                Intent intent=new Intent(ACTION);
                intent.putExtra("SuccessCount", SuccessCount);
                intent.putExtra("ErrorCount", ErrorCount);
                sendBroadcast(intent);


            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                ErrorCount++;
                Intent intent=new Intent(ACTION);
                intent.putExtra("SuccessCount", SuccessCount);
                intent.putExtra("ErrorCount", ErrorCount);
                sendBroadcast(intent);

                if(ErrorCount%interval==2){
                    //再错误的情况下，没20分钟发送一次邮件
                    String time=Utils.date2String(System.currentTimeMillis(),"yyyy-MM-dd hh:mm:ss");
                    send("时间："+time+"\n"+volleyError.getMessage());
                }
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
                try {
                    String e = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                    return Response.success(e, HttpHeaderParser.parseCacheHeaders(networkResponse));
                } catch (UnsupportedEncodingException var3) {
                    return Response.error(new ParseError(var3));
                }
            }
        };

        //jsonRequest.setRetryPolicy(new DefaultRetryPolicy());
        // 超时时间 20s ,注意:必须大于3s
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonRequest.setTag(getClass().getName());
        getRequestQueue(this).add(jsonRequest);
    }



    public  RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            synchronized (LocalService.class) {
                requestQueue = Volley.newRequestQueue(context);

                requestQueue.start();
            }
        }

        return requestQueue;
    }

    private  RequestQueue requestQueue = null;

    public void send(final String Message){
        final String receive = "1159673852@qq.com";//接收邮箱
        final String cc1 = "crt521@163.com";//抄送邮箱
        final String cc2 = null;//抄送邮箱
        final String cc3 = null;//抄送邮箱
        final String bcc = null;//密送邮箱
        final String send = "a13487037471@163.com";//发送邮箱
        final String pwd = "823371296";//邮箱密码
        final String subject = "错误日志";//发送标题
        final String message = Message;//发送文本信息
        // 建立发送邮件任务
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    // sendEmailByApacheCommonsNet(send, pwd,receive, subject, message);
                    System.out.println("send=" + send + ",pwd=" + pwd + ",receive=" + receive + ",subject=" + subject + ",message="
                            + message);
                    Utils.sendEmailByApacheCommonsEmail(send, pwd, receive, bcc, subject, message,cc1,cc2,cc3);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }.execute();
    }
}
