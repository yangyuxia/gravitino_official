/*
 * Copyright 2023 Datastrato Pvt Ltd.
 * This software is licensed under the Apache License version 2.
 */
package org.apache.gravitino.catalog.wutong;

import org.apache.gravitino.catalog.jdbc.JdbcCatalog;
import org.apache.gravitino.catalog.jdbc.converter.JdbcColumnDefaultValueConverter;
import org.apache.gravitino.catalog.jdbc.converter.JdbcExceptionConverter;
import org.apache.gravitino.catalog.jdbc.converter.JdbcTypeConverter;
import org.apache.gravitino.catalog.jdbc.operation.JdbcDatabaseOperations;
import org.apache.gravitino.catalog.jdbc.operation.JdbcTableOperations;
import org.apache.gravitino.catalog.wutong.converter.WutongColumnDefaultValueConverter;
import org.apache.gravitino.catalog.wutong.converter.WutongExceptionConverter;
import org.apache.gravitino.catalog.wutong.converter.WutongTypeConverter;
import org.apache.gravitino.catalog.wutong.operation.WutongSchemaOperations;
import org.apache.gravitino.catalog.wutong.operation.WutongTableOperations;
import org.apache.gravitino.connector.CatalogOperations;
import java.util.Map;

public class WutongCatalog extends JdbcCatalog {

  @Override
  public String shortName() {
    return "jdbc-wutong";
  }

  @Override
  protected CatalogOperations newOps(Map<String, String> config) {
    JdbcTypeConverter jdbcTypeConverter = createJdbcTypeConverter();
    return new WutongCatalogOperations(
        createExceptionConverter(),
        jdbcTypeConverter,
        createJdbcDatabaseOperations(),
        createJdbcTableOperations(),
        createJdbcColumnDefaultValueConverter());
  }

  @Override
  protected JdbcExceptionConverter createExceptionConverter() {
    return new WutongExceptionConverter();
  }

  @Override
  protected JdbcTypeConverter createJdbcTypeConverter() {
    return new WutongTypeConverter();
  }

  @Override
  protected JdbcDatabaseOperations createJdbcDatabaseOperations() {
    return new WutongSchemaOperations();
  }

  @Override
  protected JdbcTableOperations createJdbcTableOperations() {
    return new WutongTableOperations();
  }

  @Override
  protected JdbcColumnDefaultValueConverter createJdbcColumnDefaultValueConverter() {
    return new WutongColumnDefaultValueConverter();
  }
}
