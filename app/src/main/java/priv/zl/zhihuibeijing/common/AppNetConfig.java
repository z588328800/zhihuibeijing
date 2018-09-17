package priv.zl.zhihuibeijing.common;

public class AppNetConfig {
    /**
     * HOST地址
     */
    public static final String HOST = "192.168.2.103";
    /**
     * 请求基础
     */
    public static final String BASEURL = "http://" + HOST + ":8080/zhihuibeijing";
    public static final String BASEURLJSON = "http://" + HOST + ":8080/zhihuibeijing/GetJsonNewsCenter1?json_path=";
    public static final String GetJsonNewsCenter1 = BASEURLJSON + "WEB-INF/categories.json";
}
