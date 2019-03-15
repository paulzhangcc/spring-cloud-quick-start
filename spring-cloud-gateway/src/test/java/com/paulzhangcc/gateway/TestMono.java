package com.paulzhangcc.gateway;

import org.junit.Test;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;

/**
 * @author paul
 * @description
 * @date 2019/1/28
 */
public class TestMono {
    private static final Logger logger = LoggerFactory.getLogger(TestMono.class);

    public static class Result {
        private Integer num;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public void add(Integer add) {
            num = num.intValue() + add.intValue();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Mono.just("nihao").subscribe((t)->{
            System.out.println(t);
        });
        System.out.println(1);


        Boolean.TRUE.equals(false);



        WebClient.builder().baseUrl("http://www.baidu.com").build().get().uri("/")
                .exchange()
                .flatMap(clientResponse ->
                        clientResponse.bodyToMono(String.class)
                ).subscribe(System.out::println);

        Thread.sleep(Integer.MAX_VALUE);

        UnicastProcessor<Integer> processor = UnicastProcessor.create();
        FluxSink<Integer> sink = processor.sink();
        processor.subscribe(t -> {
            logger.info(t.toString());
        });



        Flux.just("1234", "56A78", "11223").flatMap(k -> Flux.just(Integer.parseInt(k))).doOnError(System.err::println).subscribe(System.out::println);

        Flux.create(i -> {
            i.next("AAAAA");
            //i.next("BBBBB");//注意generate中next只能调用1次
            i.complete();
        }).subscribe(System.out::println);


        Flux.just("1234", "5678").flatMap(str -> Flux.just(str.split(""))).collect(Result::new, new BiConsumer<Result, String>() {
            @Override
            public void accept(Result result, String s) {
                int i = Integer.parseInt(s);
                if (result.num == null) {
                    result.setNum(i);
                } else {
                    result.add(i);
                }
            }
        }).subscribe(t -> {
            System.out.println(t);
        });

//        Mono<Integer> just = Mono.just(1);
//        just.subscribe(System.out::println);
        Flux.fromArray(new String[]{"1", "2", "3", "4"})
                .publishOn(Schedulers.newSingle("test1"))
                .log("=====")
                .map(t -> Integer.parseInt("1000" + t))
                .publishOn(Schedulers.newSingle("test2"))
                .log("+++++").checkpoint()
                .window(2).map(t -> t.reduce((a, b) -> a + b))
                .publishOn(Schedulers.newSingle("test3"))
                .log("-----")
                .subscribe(t -> t.subscribe(a -> {

                    System.out.println(Thread.currentThread().getName() + ":=-" + a.intValue());
                }));
        System.out.println("==============================================");
        //        Flux<Integer> map = Flux.fromArray(new String[]{"1", "2", "3", "4"}).map(t -> Integer.parseInt(t));

//        Flux.just("A", "B", "C")
//                .concatWith(Flux.error(new IndexOutOfBoundsException("下标越界啦！")))
//                .onErrorReturn("X")
//                .subscribe(t -> {
//                    System.out.println("success:" + t);
//                }, t -> {
//                    System.out.println("error:" + t);
//                });

//        Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).window(Duration.of(1, ChronoUnit.SECONDS))
//                .parallel().subscribe(flux -> {
//            flux.reduce((a,b)->a+b).subscribe(System.out::println);
//        });
//        Flux.interval(Duration.of(100, ChronoUnit.MILLIS))
//                .bufferTimeout(100, Duration.of(2, ChronoUnit.SECONDS)).parallel(2)
//                .subscribe(t->{
//                    try {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String format = sdf.format(new Date());
//                        System.out.println(Thread.currentThread().getName()+":"+format+":"+t);
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        if (1 == 1) {
            countDownLatch.await();
        }
        //Flux.range(0, 10).buffer(3).subscribe(System.out::println);

//        Flux.error(new RuntimeException()).subscribe(consumer -> System.out.println("next:" + consumer),
//                consumer -> System.out.println("error:" + consumer),
//                ()-> System.out.println("complete"));
        Flux.generate(synchronousSink -> {
            synchronousSink.next("hello");
            //synchronousSink.error(new RuntimeException("报错了"));
            synchronousSink.complete();
        }).subscribe(next -> System.out.println(next)
                , error -> System.out.println(error.getMessage())
                , () -> System.out.println("完成")
        );

        if (1 == 1) {
            return;
        }
        Flux<Integer> ints = Flux.range(1, 4)
                .map(i -> {
                    return i;
//                    if (i != 3) {
//                        return i;
//                    } else {
//                        throw new RuntimeException("Got to 4");
//                    }
                });


//        ints.subscribe(
//                success -> System.out.println("SUCCESS:" + success)
//                ,
//                error -> System.out.println("ERROR: " + error)
//                ,
//                () -> System.out.println("DONE")
//        );


        ints.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("===============================");
                request(1);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println(":" + value);
                request(1);
            }

            @Override
            protected void hookOnComplete() {
                super.hookOnComplete();
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                super.hookOnError(throwable);
            }

            @Override
            protected void hookFinally(SignalType type) {
                super.hookFinally(type);
            }
        });

    }
    @Test
    public void testWebClient() throws InterruptedException {
        Mono<String> resp = WebClient.create()
                .method(HttpMethod.GET)
                .uri("http://127.0.0.1:9000/user/list")
                .retrieve()
                .bodyToMono(String.class);

        resp.subscribe(t->{
            logger.info("---------------"+t);
        });

        HttpClient.create().baseUrl("http://127.0.0.1:9000")
                .post().uri("/user/list")
                .response().subscribe(response->{
                    logger.info("---------------"+response);
                });

        logger.info("================");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
