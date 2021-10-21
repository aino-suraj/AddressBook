package org.test.addressbook.injection;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.test.addressbook.service.HelloServiceAsync;

public class HelloServiceAsyncImpl implements HelloServiceAsync {

  @Override
  public CompletableFuture<String> sayHello(String name) {
    CompletableFuture<String> completableFuture = new CompletableFuture<String>();

    Supplier<String> supplier = () -> {
      return name;
    };

    return completableFuture.supplyAsync(supplier);

  }

}
