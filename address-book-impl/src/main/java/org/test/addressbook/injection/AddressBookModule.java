package org.test.addressbook.injection;

import org.appops.core.annotation.ImplModule;
import org.appops.service.injection.ServiceModule;
import org.test.addressbook.core.AddressBook;
import org.test.addressbook.impl.HelloServiceAsyncImpl;
import org.test.addressbook.impl.HelloServiceImpl;
import org.test.addressbook.service.HelloService;
import org.test.addressbook.service.HelloServiceAsync;

@ImplModule(serviceName = AddressBook.class)
public class AddressBookModule extends ServiceModule {

  @Override
  public void configureModule() {
    bind(HelloService.class).to(HelloServiceImpl.class);
    bind(HelloServiceAsync.class).to(HelloServiceAsyncImpl.class);
  }

}
