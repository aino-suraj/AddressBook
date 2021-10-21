package org.test.addressbook.app;

import java.io.FileNotFoundException;
import org.appops.core.ServiceException;
import org.appops.service.entrypoint.ServiceEntryPoint;


/**
 * Service  application entry point class.
 */
public class AddressBookEntryPoint extends ServiceEntryPoint{

  /**
   * Service application entry point.
   * 
   * @param args Application parameters array.
   * @throws FileNotFoundException if config file not found.
   * @throws org.appops.core.ServiceException
   */
  public static void main(String[] args) throws FileNotFoundException, ServiceException { 
    new AddressBookEntryPoint().startService(args);  }
}
