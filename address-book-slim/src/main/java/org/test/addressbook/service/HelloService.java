package org.test.addressbook.service;

import org.appops.core.service.RequestMethod;
import org.appops.core.service.annotation.ServiceOp;
import org.test.addressbook.core.AddressBook;

@AddressBook
public interface HelloService {

  @ServiceOp(method = RequestMethod.GET, path = "sayHello")
  public String sayHello(String name);
}
