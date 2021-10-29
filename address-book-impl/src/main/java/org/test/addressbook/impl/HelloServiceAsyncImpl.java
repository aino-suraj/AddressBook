package org.test.addressbook.impl;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.test.addressbook.service.HelloServiceAsync;

public class HelloServiceAsyncImpl implements HelloServiceAsync {

  @Override
  public CompletableFuture<String> sayHello(String name) {
    CompletableFuture<String> completableFuture = new CompletableFuture<String>();

    Supplier<String> supplier = () -> {
      return "Hello " + name + " !";
    };

    return completableFuture.supplyAsync(supplier);

  }

  @Override
  public CompletableFuture<String> addName(String name) {
    CompletableFuture<String> completableFuture = new CompletableFuture<String>();

    Supplier<String> supplier = () -> {
      return "name added " + name;
    };

    return completableFuture.supplyAsync(supplier);
  }

}
