
import org.apache.log4j.Logger;
import org.darkghast.fms.utils.SavedFileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
    /**
     * 用于记录日志信息
     */
    static Logger log = Logger.getLogger(Test.class.getName());
    @org.junit.Test
    public void test1(){
        System.out.println(new Date());
    }

    @org.junit.Test
    public void test2(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());
        System.out.println(dateName);
    }

    @org.junit.Test
    public void testDelete(){
        String filename ="D:\\文件\\临时文件夹\\实训\\2\\2\\2021-06-30-08-53-45-E1F055768A9FCE558FF432128F25AFC6-7BCF43F3-4151-E6D6-CCDF-338C8DDAC53F-[yande_403494_Twinsenzw]misaki_kurehito monochrome saenai_heroine_no_sodatekata seifuku sweater_00000.png";
        if (new File(filename).delete()) {
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }
    }

    @org.junit.Test
    public void testSHA1() throws IOException {
        String filename = "D:\\文件\\临时文件夹\\实训\\2021-06-30-08-53-43-E1F055768A9FCE558FF432128F25AFC6-078939FD-3A84-7A1C-C8F1-CFD7FACB3CD1-[yande_403494_Twinsenzw]misaki_kurehito monochrome saenai_heroine_no_sodatekata seifuku sweater_00000.png";
        System.out.println(SavedFileUtils.getIdentifier(new File(filename)));
    }

    @org.junit.Test
    public void testEncode() throws UnsupportedEncodingException {
        String filename="啊";
        String filenameBytes=filename.getBytes(StandardCharsets.UTF_8).toString();
        System.out.println(filenameBytes);
        String encode1=new String(filename.getBytes(StandardCharsets.UTF_8),"ISO8859-1");
        System.out.println(encode1);
        String encode2= URLEncoder.encode(filename, "ISO8859-1");
        System.out.println(encode2);
    }

    @org.junit.Test
    public void testLog(){
        log.info("log测试");
    }
}
