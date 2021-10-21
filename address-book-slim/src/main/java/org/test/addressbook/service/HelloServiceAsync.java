package org.test.addressbook.service;

import java.util.concurrent.CompletableFuture;
import org.appops.core.annotation.AsyncOf;

@AsyncOf(sync = HelloService.class)
public interface HelloServiceAsync {

  public CompletableFuture<String> sayHello(String name);

}
