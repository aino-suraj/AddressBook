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
import org.test.addressbook.service.HelloService;
import org.test.addressbook.service.HelloServiceAsync;

public class SyncHelloServiceTest {

  private Injector injector;

  @BeforeEach
  public void setup() throws ServiceException {
    AddressBookSlimInjectorProvider injectorProvider = new AddressBookSlimInjectorProvider();
    new AddressBookImplInjectorProvider();
    injector = injectorProvider.getInjector();
  }


  @Test
  void testSyncImplementationInvocation() throws InterruptedException, ExecutionException {
    HelloService helloService = injector.getInstance(HelloService.class);
    String result = helloService.addName("suraj");
    assertEquals("name added suraj", result.trim());
  }

  @Test
  void testAsyncImplementationInvocation() throws InterruptedException, ExecutionException {
    HelloServiceAsync helloService = injector.getInstance(HelloServiceAsync.class);
    Function<String, String> getOutput = (name) -> {
      System.out.println("Name is - " + name);
      return name;
    };

    CompletableFuture<String> result = helloService.addName("suraj").thenApplyAsync(getOutput);
    if (result != null) {
      System.out.println(result);
    }

    Thread.sleep(5000);

  }

}
