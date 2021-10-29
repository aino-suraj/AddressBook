package org.test.addressbook;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Method;
import org.appops.core.service.meta.ServiceOpMeta;
import org.appops.slim.base.invocation.ApiProxyInvocationHandler;
import org.appops.slim.base.invocation.async.AsyncOpMetaComputer;
import org.junit.jupiter.api.Test;
import org.test.addressbook.service.HelloService;
import org.test.addressbook.service.HelloServiceAsync;

class AsyncOpMetaBuilderTest {

  @Test
  void testAsyncOpMeta() throws NoSuchMethodException, SecurityException, ClassNotFoundException {

    // Calculate String for HelloService -> sayHello opMeta

    Class<?> clazz = Class.forName(HelloService.class.getCanonicalName());
    Method method = clazz.getDeclaredMethod("sayHello", String.class);
    Object[] args = method.getParameters();

    ApiProxyInvocationHandler computeSync = new ApiProxyInvocationHandler();
    ServiceOpMeta syncOpMeta = computeSync.createOpMeta(method, args);

    // Calculate String for HelloServiceAsync -> sayHello opMeta

    Class<?> asyncClazz = Class.forName(HelloServiceAsync.class.getCanonicalName());
    Method asyncMethod = asyncClazz.getDeclaredMethod("sayHello", String.class);
    Object[] asyncMethodArgs = asyncMethod.getParameters();

    AsyncOpMetaComputer computeASync = new AsyncOpMetaComputer();
    ServiceOpMeta asyncOpMeta = computeASync.computeOpMeta(asyncMethod, asyncMethodArgs);

    assertEquals(syncOpMeta.getName(), asyncOpMeta.getName());
    assertEquals(syncOpMeta.getClass(), asyncOpMeta.getClass());
  }

}
