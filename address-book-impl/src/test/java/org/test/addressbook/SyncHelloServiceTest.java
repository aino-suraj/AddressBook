package org.test.addressbook;

import static org.junit.Assert.assertEquals;
import com.google.inject.Injector;
import java.util.concurrent.ExecutionException;
import org.appops.core.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.addressbook.mock.provider.AddressBookImplInjectorProvider;
import org.test.addressbook.mock.provider.AddressBookSlimInjectorProvider;
import org.test.addressbook.service.HelloService;

public class SyncHelloServiceTest {

  private Injector injector;

  @BeforeEach
  public void setup() throws ServiceException {
    AddressBookSlimInjectorProvider injectorProvider = new AddressBookSlimInjectorProvider();
    new AddressBookImplInjectorProvider();
    injector = injectorProvider.getInjector();
  }


  @Test
  void testAsyncImplementationInvocation() throws InterruptedException, ExecutionException {
    HelloService helloService = injector.getInstance(HelloService.class);
    String result = helloService.addName("suraj");
    assertEquals("name added suraj", result.trim());
  }

}
