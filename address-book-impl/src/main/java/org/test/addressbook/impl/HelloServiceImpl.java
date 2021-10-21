package org.test.addressbook.impl;

import org.test.addressbook.service.HelloService;

public class HelloServiceImpl implements HelloService {

  @Override
  public String sayHello(String name) {
    return "Hello " + name+" !";
  }

}

