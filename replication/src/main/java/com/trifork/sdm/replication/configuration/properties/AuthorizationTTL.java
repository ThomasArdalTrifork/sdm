package com.trifork.sdm.replication.configuration.properties;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import javax.inject.Qualifier;

@Qualifier
@Retention(RUNTIME)
public @interface AuthorizationTTL {}