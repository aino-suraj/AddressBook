package org.test.addressbook;

import static org.junit.Assert.assertEquals;
import com.google.inject.Injector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import org.appops.core.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.addressbook.mock.provider.AddressBookImplInjectorProvider;
import org.test.addressbook.mock.provider.AddressBookSlimInjectorProvider;
import org.test.addressbook.service.HelloServiceAsync;

public class AsyncHelloServiceTest {

  private Injector injector;

  @BeforeEach
  public void setup() throws ServiceException {
    AddressBookSlimInjectorProvider injectorProvider = new AddressBookSlimInjectorProvider();
    injector = injectorProvider.getInjector();
    new AddressBookImplInjectorProvider();
  }


  @Test
  void testAsyncInvocation() throws InterruptedException, ExecutionException {
    HelloServiceAsync helloServiceAsync = injector.getInstance(HelloServiceAsync.class);
    CompletableFuture<String> result =
        helloServiceAsync.addName("Prashant").thenApplyAsync(new Function<String, String>() {

          @Override
          public String apply(String t) {
            String result = "hi Suraj " + t;
            System.out.println(result);
            assertEquals("hi Suraj Prashant", result);
            return result;
          }
        });

    Thread.sleep(50000);
    System.out.println("sayHello() method invoked...." + result);

  }

}
