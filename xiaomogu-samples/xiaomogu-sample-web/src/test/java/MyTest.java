import org.apache.catalina.util.URLEncoder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @auther JameHou
 * @date 2019/1/18 22:35
 */
public class MyTest {

    private static  ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static void main(String[] args) throws UnsupportedEncodingException {
        //String[] arr = {"111","222","333","444"};
        URLEncoder urlEncoder = new URLEncoder();
        String url = urlEncoder.encode("http://www.cnhnb.com/", Charset.forName("UTF-8"));
        System.out.println(url);
    }


    public static String  sayhello() throws InterruptedException {
        Random random = new Random();
        long sleep = random.nextInt(100);
        System.out.println("花费时间"+sleep);
        Thread.sleep(sleep);
        System.out.println("执行完毕..........");
        return "hello，world";
    }
}
