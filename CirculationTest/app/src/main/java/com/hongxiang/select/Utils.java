package com.hongxiang.select;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fjw_zhushunqing on 2017/4/6.
 */

public class Utils {

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }



    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId="-";
        try{
            deviceId = tm.getDeviceId();
        }catch(Exception e){

        }
        return deviceId;
    }

    /**
     * 生成body体提交需要的随机数
     *
     * @param context
     * @return
     */
    public static String r(Context context) {
        StringBuffer r = new StringBuffer();
        String did = getDeviceId(context);
//        int size = did.length();
//        r.append(did.substring(size - 5, size));
//        r.append(System.currentTimeMillis());
//        return r.toString();
        return did;
    }


    /**
     * Date（long） 转换 String
     *
     * @param time
     * @param format
     * @return
     */
    public static String date2String(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(time);
        return s;
    }


    /**
     * 检测邮箱地址是否合法
     *
     * @param address
     * @return true合法 false不合法
     */
    private static boolean verifyEmailAddress(String address) {
        if (null == address || "".equals(address))
            return false;

        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(address);
        return m.matches();
    }

    /**
     * @throws EmailException
     * @Description TODO 发送带附件的email
     */
    public static void sendEmailByApacheCommonsEmail(String from, String fromPwd, String to, String bcc, String subject, String message,String... cc)
            throws EmailException {

        if (!verifyEmailAddress(from) || !verifyEmailAddress(to)) {
            System.out.println("enter verifyEmailAddress");
            return;
        }

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setDebug(true);
        // 这里使用163邮箱服务器，实际需要修改为对应邮箱服务器
        //smtp.163.com:25   smtp.qq.com:587
        email.setHostName("smtp.163.com");
//        email.setSmtpPort(465);//163邮箱25
        email.setSocketTimeout(6 * 1000);
        email.setCharset("UTF-8");
//        email.setTLS(true);
        email.setStartTLSEnabled(true);
//        email.setSSL(true);
        email.setAuthentication(from, fromPwd);
        email.addTo(to, to);
//        email.addBcc(bcc);// 密送邮箱
        email.addCc(cc);//抄送邮箱
        email.setFrom(from, from);
        email.setSubject(subject);
        email.setMsg(message);

        // Create the attachment
//        EmailAttachment attachment2 = new EmailAttachment();
//        attachment2.setPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pdf02.png");
//        attachment2.setDisposition(EmailAttachment.ATTACHMENT);
//        attachment2.setDescription("pdf02");
//        attachment2.setName("pdf02.png");

//        EmailAttachment attachment1 = new EmailAttachment();
//        attachment1.setPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pdf01.png");
//        attachment1.setDisposition(EmailAttachment.ATTACHMENT);
//        attachment1.setDescription("pdf01");
//        attachment1.setName("pdf01.png");

//        email.attach(attachment1);
//        email.attach(attachment2);

        // send the email
        String sendStr = email.send();
        System.out.println("sendStr=" + sendStr);
    }

}
