/*
 * Copyright 2023 Datastrato Pvt Ltd.
 * This software is licensed under the Apache License version 2.
 */
package org.apache.gravitino.catalog.wutong;

import org.apache.gravitino.catalog.jdbc.JdbcCatalogOperations;
import org.apache.gravitino.catalog.jdbc.converter.JdbcColumnDefaultValueConverter;
import org.apache.gravitino.catalog.jdbc.converter.JdbcExceptionConverter;
import org.apache.gravitino.catalog.jdbc.converter.JdbcTypeConverter;
import org.apache.gravitino.catalog.jdbc.operation.JdbcDatabaseOperations;
import org.apache.gravitino.catalog.jdbc.operation.JdbcTableOperations;
import java.sql.Driver;
import java.sql.DriverManager;

public class WutongCatalogOperations extends JdbcCatalogOperations {

  public WutongCatalogOperations(
      JdbcExceptionConverter exceptionConverter,
      JdbcTypeConverter jdbcTypeConverter,
      JdbcDatabaseOperations databaseOperation,
      JdbcTableOperations tableOperation,
      JdbcColumnDefaultValueConverter columnDefaultValueConverter) {
    super(
        exceptionConverter,
        jdbcTypeConverter,
        databaseOperation,
        tableOperation,
        columnDefaultValueConverter);
  }

  @Override
  public void close() {
    super.close();
    try {
      // Unload the PostgreSQL driver, only Unload the driver if it is loaded by
      // IsolatedClassLoader.
      Driver pgDriver = DriverManager.getDriver("jdbc:postgresql://dummy_address:12345/");
      deregisterDriver(pgDriver);
    } catch (Exception e) {
      LOG.warn("Failed to deregister PostgreSQL driver", e);
    }
  }
}
