package com.hongxiang.select;



/**
 * Created by Administrator on 2016/6/12.
 */
public class APIData {

    /**
     * 方法名method
     */
    private String M = "";
    /**
     * 手机设备ID
     */
    private String DID = "TEST";
    /**
     * 时间戳，系统时间的秒值,同个应用的不同api请求的time值应该是递增的, 用于防replay攻击
     */
    private long TS = 0;
    /**
     * 请求随机ID - Guid
     */
    private String G = "";
    /**
     * 手机客户端版本号 - version
     */
    private int V = 1;
    /**
     * 业务数据JSON格式 - data
     */
    private String D = "";
    /**
     * 用户登录后的token，用于用户权限验证
     */
    private String T = "";
    /**
     * 方法是否需要加密
     */
    private boolean IE = true;
    /**
     * 加密版本号
     */
    private int E;
    /**
     * platform
     */
    private int P = 1;

    public String getM() {
        return M;
    }

    public void setM(String m) {
        M = m;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public long getTS() {
        return TS;
    }

    public void setTS(long TS) {
        this.TS = TS;
    }

    public String getG() {
        return G;
    }

    public void setG(String g) {
        G = g;
    }

    public int getV() {
        return V;
    }

    public void setV(int v) {
        V = v;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getT() {
        return T;
    }

    public void setT(String t) {
        T = t;
    }

    public boolean isIE() {
        return IE;
    }

    public void setIE(boolean IE) {
        this.IE = IE;
    }

    public int getE() {
        return E;
    }

    public void setE(int e) {
        E = e;
    }

    public int getP() {
        return P;
    }

    public void setP(int p) {
        P = p;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
