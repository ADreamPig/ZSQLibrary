package com.hongxiang.select;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
private TextView mStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatus=(TextView) findViewById(R.id.status);

        MyReceiver receiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(LocalService.ACTION);
        registerReceiver(receiver,intentFilter);


    }

    public void start(View view){
        PollingUtils.startPollingService(this, 50, LocalService.class, LocalService.ACTION);
//        startService(new Intent(this,LocalService.class));
    }

    public void stop(View view){
        PollingUtils.stopPollingService(this, LocalService.class, LocalService.ACTION);
        mStatus.setText("");
    }

    public void send(){
        final String receive = "1159673852@qq.com";//接收邮箱
        final String cc = null;//抄送邮箱
        final String bcc = null;//密送邮箱
        final String send = "a13487037471@163.com";//发送邮箱
        final String pwd = "823371296";//邮箱密码
        final String subject = "错误日志";//发送标题
        final String message = "服务器宕机了";//发送文本信息
        // 建立发送邮件任务
        new AsyncTask<String, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    // sendEmailByApacheCommonsNet(send, pwd,receive, subject, message);
                    System.out.println("send=" + send + ",pwd=" + pwd + ",receive=" + receive + ",subject=" + subject + ",message="
                            + message);
                    Utils.sendEmailByApacheCommonsEmail(send, pwd, receive, cc, bcc, subject, message);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }.execute();
    }
    private int SuccessCount, ErrorCount;

   public  class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
             SuccessCount=intent.getIntExtra("SuccessCount",0);
             ErrorCount=intent.getIntExtra("ErrorCount",0);
            if(SuccessCount+ErrorCount==800){
                mStatus.setText("");
            }
            if(ErrorCount%20==2){
                //再错误的情况下，没20分钟发送一次邮件
//                send();
            }
            if(mStatus!=null){
               String temp= mStatus.getText().toString();
                String time=Utils.date2String(System.currentTimeMillis(),"yyyy-MM-dd hh:mm:ss");

                temp+="正确次数："+SuccessCount+"  错误次数："+ErrorCount+"   "+time+"\n";
                mStatus.setText(temp);
            }
        }
    }
}
