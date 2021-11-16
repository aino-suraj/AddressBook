package org.test.addressbook.service;

import org.appops.core.service.RequestMethod;
import org.appops.core.service.annotation.ServiceOp;
import org.test.addressbook.core.AddressBook;

@AddressBook
public interface HelloService {

  @ServiceOp(method = RequestMethod.POST, path = "sayHello")
  public String sayHello(String name);

  @ServiceOp(method = RequestMethod.POST, path = "addName")
  public String addName(String name);

}
