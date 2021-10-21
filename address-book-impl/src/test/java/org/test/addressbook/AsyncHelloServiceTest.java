package org.test.addressbook;

import com.google.inject.Injector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;
import org.appops.core.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.addressbook.mock.provider.AddressBookMockInjectorProvider;
import org.test.addressbook.service.HelloServiceAsync;

public class AsyncHelloServiceTest {

  private Injector injector;

  @BeforeEach
  public void setup() throws ServiceException {
    AddressBookMockInjectorProvider injectorProvider = new AddressBookMockInjectorProvider();
    injector = injectorProvider.getInjector();
  }


  @Test
  void testAsyncInvocation() throws InterruptedException, ExecutionException {
    HelloServiceAsync helloService = injector.getInstance(HelloServiceAsync.class);
    CompletableFuture<String> result =
        helloService.sayHello("Prashant").thenApply(new Function<String, String>() {

          @Override
          public String apply(String t) {
            String res = "hi Suraj " + t;
            System.out.println(res);
            return res;
          }

        });

    System.out.println("sayHello() method invoked....");

    Supplier<String> consumer = () -> {
      return "hello";
    };

    result.supplyAsync(consumer);

  }

}
