package com.paulzhangcc.gateway;

import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author paul
 * @description
 * @date 2019/3/19
 */
public class TestNettyReactiveWebServerFactory {
    public static void main(String[] args) throws IOException {
//        ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter((
//                request, response) -> {
//            return Mono.empty();
//        });
//
//        HttpServer.create()
//                .port(9999)
//                .host("0.0.0.0")
//                //.handle((req, res) -> res.sendString(Flux.just("hello")))
//                //.handle(handlerAdapter)
//                .bindNow()
//                .onDispose()
//                .block();

//        ReactiveWebServerFactory factory = new NettyReactiveWebServerFactory(9999);
//        WebServer webServer = factory.getWebServer((request, response) -> {
//            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//            return Mono.empty();
//        });
//        webServer.start();
//
//        if (true) {
//            return;
//        }
//        TcpServer tcpServer = TcpServer.create().addressSupplier(() -> new InetSocketAddress(9999));
//
//        tcpServer.bootstrap(b->{
//            Object httpServerConf = b.config().attrs().get(AttributeKey.newInstance("httpServerConf"));
//            if (httpServerConf == null){
//                new HttpServerConfiguration();
//                b.config().attrs().put(AttributeKey.newInstance("httpServerConf"));
//            }
//        }); config().
//                .attrs()
//                .get(CONF_KEY);
//
//        if (hcc == null) {
//            hcc = new HttpServerConfiguration();
//            b.attr(CONF_KEY, hcc);
//        }


        HttpServer httpServerBind = HttpServer.create();
        HttpServer httpServerTcpConfig = httpServerBind.tcpConfiguration(
                tcpServer -> tcpServer.addressSupplier(() -> new InetSocketAddress(9999))
        );
        HttpServer httpServerTcpConfig1 = httpServerTcpConfig.protocol(new HttpProtocol[]{HttpProtocol.HTTP11});
        HttpServer httpServerTcpConfig2 = httpServerTcpConfig1.forwarded(false);

        ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter((
                request, response) -> {
            return Mono.empty();
        });

        HttpServer httpServerHandle = httpServerTcpConfig2.handle(handlerAdapter);
        DisposableServer disposableServer = httpServerHandle.bindNow();

        Thread thread = new Thread(() -> {
            disposableServer.onDispose().block();
        });
        thread.setDaemon(false);
        thread.start();


    }
}
