package org.darkghast.fms.utils;

/**
 * 用于获取url的固定前缀
 */
public class UrlUtils {
    /**
     * url的前缀
     */
    private static String DOMAIN;

    public void setDOMAIN(String domain){
        this.DOMAIN = domain;
    }

    public static String getDOMAIN(){
        return DOMAIN;
    }
}
