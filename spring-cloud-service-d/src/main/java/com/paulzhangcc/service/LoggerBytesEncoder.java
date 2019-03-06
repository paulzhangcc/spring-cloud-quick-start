package com.paulzhangcc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.zipkin2.ZipkinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.Span;
import zipkin2.codec.BytesEncoder;
import zipkin2.codec.Encoding;
import zipkin2.codec.SpanBytesEncoder;

import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/3/6
 */
@Configuration
public class LoggerBytesEncoder {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Bean
    public BytesEncoder<Span> spanBytesEncoder(ZipkinProperties zipkinProperties) {
        SpanBytesEncoder encoder = zipkinProperties.getEncoder();
        return new BytesEncoder<Span>(){

            @Override
            public Encoding encoding() {
                return encoder.encoding();
            }

            @Override
            public int sizeInBytes(Span input) {
                return encoder.sizeInBytes(input);
            }

            @Override
            public byte[] encode(Span input) {
                LOGGER.warn("\r\n"+input);
                return encoder.encode(input);
            }

            @Override
            public byte[] encodeList(List<Span> input) {
                LOGGER.warn("\r\n"+input);
                return encoder.encodeList(input);
            }
        };
    }

}
