package org.darkghast.fms.utils;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * 用于获取GUID的工具类
 * GUID是对UUID的实现
 */
public class GUIDUtils {
    /**
     * 使用伪随机数获取GUID
     *
     * @return GUID
     */
    public static String getGUID() {
        return getGUID(false);
    }

    /**
     * 获取GUID
     *
     * @param secure 是否使用真随机数获取GUID
     * @return GUID
     */
    public static String getGUID(boolean secure) {
        return new RandomGUID(secure).toString();
    }

    /**
     * 获取UUID
     *
     * @return UUID
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}

class RandomGUID {

    public String valueBeforeMD5 = "";
    public String valueAfterMD5 = "";
    private static final Random myRand;
    private static final SecureRandom mySecureRand;

    private static String s_id;

    static {
        mySecureRand = new SecureRandom();
        long secureInitializer = mySecureRand.nextLong();
        myRand = new Random(secureInitializer);
        try {
            s_id = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public RandomGUID() {
        getRandomGUID(false);
    }

    public RandomGUID(boolean secure) {
        getRandomGUID(secure);
    }

    private void getRandomGUID(boolean secure) {
        MessageDigest md5 = null;
        StringBuffer sbValueBeforeMD5 = new StringBuffer();

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
        }

        try {
            long time = System.currentTimeMillis();
            long rand = 0;

            if (secure) {
                rand = mySecureRand.nextLong();
            } else {
                rand = myRand.nextLong();
            }

            sbValueBeforeMD5.append(s_id);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(time);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(rand);

            valueBeforeMD5 = sbValueBeforeMD5.toString();
            md5.update(valueBeforeMD5.getBytes());

            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer();
            for (byte value : array) {
                int b = value & 0xFF;
                if (b < 0x10)
                    sb.append('0');
                sb.append(Integer.toHexString(b));
            }

            valueAfterMD5 = sb.toString();

        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }

    public String toString() {
        String raw = valueAfterMD5.toUpperCase();
        StringBuffer sb = new StringBuffer();
        sb.append(raw, 0, 8);
        sb.append("-");
        sb.append(raw, 8, 12);
        sb.append("-");
        sb.append(raw, 12, 16);
        sb.append("-");
        sb.append(raw, 16, 20);
        sb.append("-");
        sb.append(raw.substring(20));

        return sb.toString();
    }
}