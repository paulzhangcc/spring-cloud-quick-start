import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by paul on 2019/2/16.
 */
public class Test {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1, 2, 0, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>()
        );
        Future<?> submit = threadPoolExecutor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        submit.cancel(true);


//        while (true){
//            InetUtils inetUtils = new InetUtils(new InetUtilsProperties());
//            String hostname = inetUtils.findFirstNonLoopbackHostInfo().getHostname();
//            System.out.println(hostname);
//            try {
//                Thread.currentThread().sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }



    }
}
