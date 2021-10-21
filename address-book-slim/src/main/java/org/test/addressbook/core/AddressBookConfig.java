package org.test.addressbook.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.appops.core.annotation.Config;

@Config
@AddressBook
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressBookConfig {
  /*
   * Add this annotation on top of configuration classes which you will create to provide
   * configurations.
   */
}
