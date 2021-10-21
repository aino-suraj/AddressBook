package org.test.addressbook.core;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.appops.core.service.annotation.Service;

@Service
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressBook {

}
