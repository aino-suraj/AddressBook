package org.test.addressbook.injection;

import org.appops.core.annotation.SlimModule;
import org.appops.slim.base.injection.ServiceSlimModule;
import org.test.addressbook.core.AddressBook;
import org.test.addressbook.service.HelloService;
import org.test.addressbook.service.HelloServiceAsync;

@SlimModule(serviceName = AddressBook.class)
public class AddressBookSlimModule extends ServiceSlimModule {

  @Override
  public void configureModule() {
    bindServiceApi(HelloService.class);
    bindAsyncServiceApi(HelloServiceAsync.class);
  }

}
