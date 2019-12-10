package com.whs.wework;

public class WeworkConfig {
    public String corpid="ww89219670ad1e9a4e";
    String corpsecret="4flRSUlS1HteEQ9yQaqcKHmN3_Q10TSkPn7E5sJh0YM";
    String contactSecret="_YyJN7ys2GW58pQ8Z9ML4OBeBAO6PSxaqX6bJK2aHtI";

    private WeworkConfig(){}
    private static WeworkConfig weworkConfig;
    public static WeworkConfig getInstance(){
        if(weworkConfig==null){
            weworkConfig=new WeworkConfig();
        }
        return weworkConfig;
    }
}
