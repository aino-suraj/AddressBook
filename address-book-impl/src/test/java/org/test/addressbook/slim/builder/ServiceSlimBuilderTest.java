package org.test.addressbook.slim.builder;

import com.google.inject.Injector;
import org.appops.cache.slim.Cache;
import org.appops.core.ServiceException;
import org.appops.service.header.constant.HttpHeaders;
import org.appops.service.slim.builder.ServiceSlimBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.addressbook.mock.provider.AddressBookImplInjectorProvider;
import org.test.addressbook.mock.provider.AddressBookSlimInjectorProvider;

class ServiceSlimBuilderTest {

  private Injector injector;

  @BeforeEach
  public void setup() throws ServiceException {
    AddressBookSlimInjectorProvider injectorProvider = new AddressBookSlimInjectorProvider();
    injector = injectorProvider.getInjector();
    new AddressBookImplInjectorProvider();
  }

  @Test
  void testServiceSlimBuilder() {

    ServiceSlimBuilder serviceSlimBuilder = injector.getInstance(ServiceSlimBuilder.class);

    serviceSlimBuilder.getServiceInstance(Cache.class).addHeader(HttpHeaders.ACCEPT, "true");

    Cache cacheService = (Cache) serviceSlimBuilder.build();
    cacheService.invalidate("test");

  }

}
