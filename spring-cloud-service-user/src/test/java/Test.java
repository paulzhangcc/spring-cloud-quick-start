import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;

/**
 * Created by paul on 2019/2/16.
 */
public class Test {
    public static void main(String[] args) {
        while (true){
            InetUtils inetUtils = new InetUtils(new InetUtilsProperties());
            String hostname = inetUtils.findFirstNonLoopbackHostInfo().getHostname();
            System.out.println(hostname);
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }
}
