/*
 * Copyright 2023 Datastrato Pvt Ltd.
 * This software is licensed under the Apache License version 2.
 */
package org.apache.gravitino.catalog.wutong.converter;

import org.apache.gravitino.catalog.jdbc.converter.JdbcExceptionConverter;
import org.apache.gravitino.exceptions.GravitinoRuntimeException;
import org.apache.gravitino.exceptions.NoSuchSchemaException;
import org.apache.gravitino.exceptions.NoSuchTableException;
import org.apache.gravitino.exceptions.SchemaAlreadyExistsException;
import org.apache.gravitino.exceptions.TableAlreadyExistsException;
import java.sql.SQLException;

public class WutongExceptionConverter extends JdbcExceptionConverter {

  @SuppressWarnings("FormatStringAnnotation")
  @Override
  public GravitinoRuntimeException toGravitinoException(SQLException se) {
    if (null != se.getSQLState()) {
      switch (se.getSQLState()) {
        case "42P04":
        case "42P06":
          return new SchemaAlreadyExistsException(se.getMessage(), se);
        case "42P07":
          return new TableAlreadyExistsException(se.getMessage(), se);
        case "3D000":
        case "3F000":
          return new NoSuchSchemaException(se.getMessage(), se);
        case "42P01":
          return new NoSuchTableException(se.getMessage(), se);
        default:
          return new GravitinoRuntimeException(se.getMessage(), se);
      }
    } else {
      return new GravitinoRuntimeException(se.getMessage(), se);
    }
  }
}
