import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by paul on 2019/2/16.
 */
public class Test {
    public static long value = 0;

    public static void main3() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition j = reentrantLock.newCondition();
        Condition o = reentrantLock.newCondition();
        new Thread(new Runnable() {
            private boolean stop = false;
            @Override
            public void run() {
                while (Thread.currentThread().isInterrupted() || !stop){
                    try{
                        reentrantLock.lock();
                        if (value == 20){
                            stop = true;
                            continue;
                        }

                        if (value%2 == 0){
                            System.out.println("偶数:"+value);
                            value++;
                            j.signalAll();
                        }else {
                            try {
                                o.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }finally {
                        reentrantLock.unlock();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            private boolean stop = false;
            @Override
            public void run() {
                while (Thread.currentThread().isInterrupted() || !stop){
                    try{
                        reentrantLock.lock();
                        if (value == 20){
                            stop = true;
                            continue;
                        }

                        if (value%2 != 0){
                            System.out.println("奇数:"+value);
                            value++;
                            o.signalAll();
                        }else {
                            try {
                                j.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }finally {
                        reentrantLock.unlock();
                    }
                }
            }
        }).start();

    }


    public static void main2() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        new Thread(new Runnable() {
            private boolean stop = false;
            @Override
            public void run() {
                while (Thread.currentThread().isInterrupted() || !stop){
                    try{
                        reentrantLock.lock();
                        if (value == 20){
                            stop = true;
                            continue;
                        }

                        if (value%2 == 0){
                            System.out.println("偶数:"+value);
                            value++;
                            condition.signalAll();
                        }else {
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }finally {
                        reentrantLock.unlock();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            private boolean stop = false;
            @Override
            public void run() {
                while (Thread.currentThread().isInterrupted() || !stop){
                    try{

                        reentrantLock.lock();
                        if (value == 20){
                            stop = true;
                            continue;
                        }

                        if (value%2 != 0){
                            System.out.println("奇数:"+value);
                            value++;
                            condition.signalAll();
                        }else {
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }finally {
                        reentrantLock.unlock();
                    }
                }
            }
        }).start();

    }

    public static void main1() {
        final Object flag = new Object();
        new Thread(new Runnable() {
            private boolean stop = false;
            @Override
            public void run() {
                while (Thread.currentThread().isInterrupted() || !stop){
                    synchronized (flag){
                        if (value == 20){
                            stop = true;
                            continue;
                        }

                        if (value%2 == 0){
                            System.out.println("偶数:"+value);
                            value++;
                            flag.notify();
                        }else {
                            try {
                                flag.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }).start();

        new Thread(new Runnable() {
            private boolean stop = false;
            @Override
            public void run() {
                while (Thread.currentThread().isInterrupted() || !stop){

                    synchronized (flag){
                        if (value == 20){
                            stop = true;
                            continue;
                        }
                        if (value%2 != 0){
                            System.out.println("奇数:"+value);
                            value++;
                            flag.notify();
                        }else {
                            try {
                                flag.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }
    public static void main(String[] args) {
        //main2();
        main3();

//        ConcurrentMap<String, String> overriddenInstanceStatusMap = CacheBuilder
//                .newBuilder().initialCapacity(500)
//                .expireAfterAccess(1, TimeUnit.HOURS)
//                .<String, String>build().asMap();
        //System.out.println(overriddenInstanceStatusMap);

//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
//                1, 2, 0, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>()
//        );
//        Future<?> submit = threadPoolExecutor.submit(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        submit.cancel(true);


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
