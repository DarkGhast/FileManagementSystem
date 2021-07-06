package org.darkghast.fms.utils;

import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 文件管理工具类
 */
public class SavedFileUtils {
    private static String ROOT_PATH;

    /**
     * 获取yyyy-MM-dd-HH-mm-ss格式的时间
     *
     * @return yyyy-MM-dd-HH-mm-ss格式的时间的字符串
     */
    public static String getTimeAsString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取当前时间
     *
     * @return 当前的时间
     */
    public static Date getTime() {
        return new Date();
    }

    /**
     * 获取Session的ID
     *
     * @param session Session对象
     * @return Session的ID
     */
    public static String getSessionID(HttpSession session) {
        return session.getId();
    }

    /**
     * 获取保存文件的前缀
     *
     * @param session Session的ID
     * @return 保存文件的前缀
     */
    public static String getPrefix(HttpSession session) {
        return getTimeAsString() + "-" + getSessionID(session) + "-" + GUIDUtils.getGUID() + "-";
    }

    /**
     * 获取数据的MD5
     *
     * @param file 输入的文件
     * @return 数据的MD5
     */
    public static String getMD5(File file) throws IOException {
        return getMD5(new FileInputStream(file));
    }

    /**
     * 获取数据的MD5
     *
     * @param data 比特流的数据
     * @return 数据的MD5
     */
    public static String getMD5(byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * 获取数据的MD5
     *
     * @param data 数据的字符串
     * @return 数据的MD5
     */
    public static String getMD5(String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * 获取数据的MD5
     *
     * @param data 数据的输入流
     * @return 数据的MD5
     */
    public static String getMD5(FileInputStream data) throws IOException {
        return DigestUtils.md5Hex(data);
    }

    /**
     * 获取数据的Sha1
     * @param file 输入的文件
     * @return 数据的Sha1
     */
    public static String getSHA1(File file) throws IOException {
        return getMD5(new FileInputStream(file));
    }

    /**
     * 获取数据的Sha1
     * @param data 数据的字符串
     * @return 数据的Sha1
     */
    public static String getSHA1(String data){
        return DigestUtils.sha1Hex(data);
    }

    /**
     * 获取数据的Sha1
     *
     * @param data 比特流的数据
     * @return 数据的Sha1
     */
    public static String getSHA1(byte[] data){
        return DigestUtils.sha1Hex(data);
    }

    /**
     * 获取数据的Sha1
     *
     * @param data 数据的输入流
     * @return 数据的Sha1
     */
    public static String getSHA1(FileInputStream data) throws IOException {
        return DigestUtils.sha1Hex(data);
    }

    /**
     * 获取文件的大小
     * @param file 输入的文件
     * @return 文件的大小
     */
    public static long getSize(File file){
        return file.length();
    }

    /**
     * 计算出文件的识别码
     * @param file 输入的文件
     * @return 文件的识别码
     */
    public static String getIdentifier(File file) throws IOException {
        return getMD5(file)+"-"+getSHA1(file)+"-"+getSize(file);
    }

    /**
     * 根据文件名，创建新的多级目录
     *
     * @param fileName 文件名
     * @return 新路径
     */
    public static String getRandomSavePath(String fileName) {
        String oldPath = ROOT_PATH;
        int hashCode = fileName.hashCode();
        // 取hashCode的二进制后四位转十进制做为二级路径
        int path1 = hashCode & 0xF;
        // 取hashCode的二进制后八位到四位转十进制做为二级路径
        int path2 = (hashCode >> 4) & 0xF;
        String newPath = oldPath + File.separator + path1 + File.separator + path2;
        File dir = new File(newPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return path1 + File.separator + path2;
    }

    public void setRootPath(String rootpath){
        this.ROOT_PATH = rootpath;
    }

    public static String getRootPath(){
        return ROOT_PATH;
    }
}
