package com.paulzhangcc.gateway.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.core.DebuggingClassWriter;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author paul
 * @description
 * @date 2019/3/13
 */
public class AopTargetUtils {
    private final static Logger logger = LoggerFactory.getLogger(AopTargetUtils.class);

    public static Object getTarget(Object proxy) {
        if (proxy == null) {
            return null;
        }
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        return getTarget(_getTarget(proxy));
    }


    private static Object _getTarget(Object proxy) {
        if (proxy instanceof Advised) {
            TargetSource targetSource = ((Advised) proxy).getTargetSource();
            try {
                logger.info("get target object from Advised#getTargetSource["+proxy.getClass().getName()+"]");
                return targetSource == null ? null : targetSource.getTarget();
            } catch (Exception e) {
                logger.error("get target object from Advised#getTargetSource["+proxy.getClass().getName()+"] error", e);
            }
        }
        if (AopUtils.isCglibProxy(proxy)) {
            try {
                return getCglibProxyTargetObject(proxy);
            } catch (Exception e) {
                logger.error("get target object from Cglib ["+proxy.getClass().getName()+"] error", e);
            }
        } else if (AopUtils.isJdkDynamicProxy(proxy)) {
            try {
                return getJdkDynamicProxyTargetObject(proxy);
            } catch (Exception e) {
                logger.error("get target object from Jdk ["+proxy.getClass().getName()+"] error", e);
            }
        }
        return null;
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        logger.info("get target object ["+target.getClass().getName()+"] from Cglib reflect ,from proxy:"+proxy.getClass().getName());
        return target;
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        logger.info("get target object ["+target.getClass().getName()+"] from Jdk reflect ,from proxy:"+proxy.getClass().getName());
        return target;
    }

    public static void main(String[] args) {
        //一下测试结果包含重复代理生成类
        {
            //cglib proxy from Advised#getTargetSource
            System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "F:\\code\\spring-cloud\\spring-cloud-quick-start\\spring-cloud-gateway\\target");
            Person person = new Person();
            ProxyFactory proxyFactory = new ProxyFactory(person);
            Object proxy = proxyFactory.getProxy();
            System.out.println(person == getTarget(proxy));

            ProxyFactory proxyFactory1 = new ProxyFactory(proxy);
            Object proxy1 = proxyFactory1.getProxy();
            System.out.println(person == getTarget(proxy1));
        }

        {
            //jdk proxy from Advised#getTargetSource
            System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
            ArrayList arrayList = new ArrayList();
            ProxyFactory proxyFactory = new ProxyFactory(arrayList);
            Object proxy = proxyFactory.getProxy();
            System.out.println(arrayList == getTarget(proxy));

            ProxyFactory proxyFactory1 = new ProxyFactory(proxy);
            Object proxy1 = proxyFactory1.getProxy();
            System.out.println(arrayList == getTarget(proxy1));
        }

        {
            //cglib proxy from reflection
            System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "F:\\code\\spring-cloud\\spring-cloud-quick-start\\spring-cloud-gateway\\target");
            Person person = new Person();
            ProxyFactory proxyFactory = new ProxyFactory(person);
            proxyFactory.setOpaque(true);
            Object proxy = proxyFactory.getProxy();
            System.out.println(person == getTarget(proxy));

            ProxyFactory proxyFactory1 = new ProxyFactory(proxy);
            proxyFactory1.setOpaque(true);
            Object proxy1 = proxyFactory1.getProxy();
            System.out.println(person == getTarget(proxy1));
        }

        {
            //jdk proxy from reflection
            System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
            ArrayList arrayList = new ArrayList();
            ProxyFactory proxyFactory = new ProxyFactory(arrayList);
            proxyFactory.setOpaque(true);
            Object proxy = proxyFactory.getProxy();
            System.out.println(arrayList == getTarget(proxy));

            ProxyFactory proxyFactory1 = new ProxyFactory(proxy);
            proxyFactory1.setOpaque(true);
            Object proxy1 = proxyFactory1.getProxy();
            System.out.println(arrayList == getTarget(proxy1));
        }

    }

    public static class Person {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;
    }

}
