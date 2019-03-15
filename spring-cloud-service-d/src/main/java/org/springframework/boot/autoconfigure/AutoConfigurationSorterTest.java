package org.springframework.boot.autoconfigure;

import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/3/8
 */
public class AutoConfigurationSorterTest {
    public static void main(String[] args) {
        String configurations = "org.springframework.cloud.sleuth.annotation.SleuthAnnotationAutoConfiguration," +
                "org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration," +
                "org.springframework.cloud.sleuth.log.SleuthLogAutoConfiguration," +
                "org.springframework.cloud.sleuth.propagation.SleuthTagPropagationAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.web.TraceHttpAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.web.TraceWebAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.web.client.TraceWebClientAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.web.client.TraceWebAsyncClientAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.async.AsyncAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.async.AsyncCustomAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.async.AsyncDefaultAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.scheduling.TraceSchedulingAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.web.client.feign.TraceFeignClientAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.hystrix.SleuthHystrixAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.rxjava.RxJavaAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.reactor.TraceReactorAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.web.TraceWebFluxAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.zuul.TraceZuulAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.grpc.TraceGrpcAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.messaging.TraceMessagingAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.messaging.TraceSpringIntegrationAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.messaging.websocket.TraceWebSocketAutoConfiguration," +
                "org.springframework.cloud.sleuth.instrument.opentracing.OpentracingAutoConfiguration";

        sort(Arrays.asList(configurations.split(","))).forEach(v-> System.out.println(v));
    }

    public static List<String> sort(List<String> data) {
        return
                new AutoConfigurationSorter(
                        new SimpleMetadataReaderFactory(), AutoConfigurationMetadataLoader
                        .loadMetadata(Thread.currentThread().getContextClassLoader())).getInPriorityOrder(data);
    }
}
