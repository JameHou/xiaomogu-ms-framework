import java.util.Random;
import java.util.concurrent.*;

/**
 * @auther JameHou
 * @date 2019/1/18 22:35
 */
public class MyTest {

    private static  ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        Future<String> future = executorService.submit(()->{
            return sayHello();
        });
       /* try {
            future.get(100L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            future.cancel(true);
            e.printStackTrace();
        }*/
        future.get(1000L, TimeUnit.MILLISECONDS);
        future.get(1000L, TimeUnit.MILLISECONDS);
        System.out.println("houzhen.....");

        executorService.shutdown();

    }


    public static String  sayHello() throws InterruptedException {
        Random random = new Random();
        long sleep = random.nextInt(100);
        System.out.println("花费时间"+sleep);
        Thread.sleep(sleep);
        System.out.println("执行完毕..........");
        return "hello，world";
    }
}
