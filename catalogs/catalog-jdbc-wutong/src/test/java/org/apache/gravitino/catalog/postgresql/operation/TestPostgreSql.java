/*
 * Copyright 2023 Datastrato Pvt Ltd.
 * This software is licensed under the Apache License version 2.
 */
package org.apache.gravitino.catalog.postgresql.operation;

import org.apache.gravitino.catalog.jdbc.TestJdbc;
import org.apache.gravitino.catalog.jdbc.config.JdbcConfig;
import org.apache.gravitino.catalog.wutong.converter.WutongColumnDefaultValueConverter;
import org.apache.gravitino.catalog.wutong.converter.WutongExceptionConverter;
import org.apache.gravitino.catalog.wutong.converter.WutongTypeConverter;
import org.apache.gravitino.catalog.wutong.operation.WutongSchemaOperations;
import org.apache.gravitino.catalog.wutong.operation.WutongTableOperations;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgreSql extends TestJdbc {

  public static final String DEFAULT_POSTGRES_IMAGE = "postgres:13";

  @BeforeAll
  public static void startup() throws Exception {
    CONTAINER =
        new PostgreSQLContainer<>(DEFAULT_POSTGRES_IMAGE)
            .withDatabaseName(TEST_DB_NAME)
            .withUsername("root")
            .withPassword("root");
    DATABASE_OPERATIONS = new WutongSchemaOperations();
    JDBC_EXCEPTION_CONVERTER = new WutongExceptionConverter();
    TestJdbc.startup();
    String jdbcUrl = CONTAINER.getJdbcUrl();
    try {
      String database =
          new URI(CONTAINER.getJdbcUrl().substring(jdbcUrl.lastIndexOf("/") + 1, jdbcUrl.length()))
              .getPath();
      Map<String, String> config =
          new HashMap<String, String>() {
            {
              put(JdbcConfig.JDBC_DATABASE.getKey(), database);
            }
          };
      TABLE_OPERATIONS = new WutongTableOperations();
      DATABASE_OPERATIONS.initialize(DATA_SOURCE, JDBC_EXCEPTION_CONVERTER, config);
      TABLE_OPERATIONS.initialize(
          DATA_SOURCE,
          JDBC_EXCEPTION_CONVERTER,
          new WutongTypeConverter(),
          new WutongColumnDefaultValueConverter(),
          config);
      DATABASE_OPERATIONS.create(TEST_DB_NAME, null, null);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
