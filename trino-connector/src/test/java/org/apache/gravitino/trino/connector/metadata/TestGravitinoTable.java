/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.gravitino.trino.connector.metadata;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.trino.spi.connector.ConnectorTableMetadata;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.gravitino.Audit;
import org.apache.gravitino.rel.Column;
import org.apache.gravitino.rel.Table;
import org.apache.gravitino.rel.expressions.distributions.Distribution;
import org.apache.gravitino.rel.expressions.distributions.Distributions;
import org.apache.gravitino.rel.expressions.sorts.SortOrder;
import org.apache.gravitino.rel.expressions.transforms.Transform;
import org.apache.gravitino.rel.types.Types;
import org.apache.gravitino.trino.connector.catalog.CatalogConnectorMetadataAdapter;
import org.apache.gravitino.trino.connector.catalog.hive.HiveMetadataAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGravitinoTable {

  @Test
  public void testGravitinoTable() {
    Column[] columns = {
      Column.of("f1", Types.StringType.get(), "f1 column"), Column.of("f2", Types.IntegerType.get())
    };
    Map<String, String> properties = new HashMap<>();
    properties.put("format", "TEXTFILE");
    Table mockTable = mockTable("table1", columns, "test table", properties);

    GravitinoTable table = new GravitinoTable("db1", "table1", mockTable);

    Assertions.assertEquals(table.getName(), mockTable.name());
    Assertions.assertEquals(table.getSchemaName(), "db1");
    Assertions.assertEquals(table.getColumns().size(), mockTable.columns().length);
    for (int i = 0; i < table.getColumns().size(); i++) {
      Assertions.assertEquals(table.getColumns().get(i).getName(), mockTable.columns()[i].name());
    }
    Assertions.assertEquals(table.getComment(), mockTable.comment());
    Assertions.assertEquals(table.getProperties(), mockTable.properties());

    CatalogConnectorMetadataAdapter adapter =
        new HiveMetadataAdapter(
            Collections.emptyList(), Collections.emptyList(), Collections.emptyList());

    ConnectorTableMetadata tableMetadata = adapter.getTableMetadata(table);
    Assertions.assertEquals(tableMetadata.getColumns().size(), table.getColumns().size());
    Assertions.assertEquals(tableMetadata.getTableSchema().getTable().getSchemaName(), "db1");
    Assertions.assertEquals(
        tableMetadata.getTableSchema().getTable().getTableName(), table.getName());

    for (int i = 0; i < table.getColumns().size(); i++) {
      Assertions.assertEquals(
          tableMetadata.getColumns().get(i).getName(), table.getColumns().get(i).getName());
    }
    Assertions.assertTrue(tableMetadata.getComment().isPresent());
    Assertions.assertEquals(tableMetadata.getComment().get(), mockTable.comment());
  }

  @Test
  public void testGravitinoTableWithOutComment() {
    Column[] columns = {
      Column.of("f1", Types.StringType.get(), "f1 column"), Column.of("f2", Types.IntegerType.get())
    };
    Map<String, String> properties = new HashMap<>();
    properties.put("format", "TEXTFILE");

    Table mockTable = mockTable("table1", columns, null, properties);

    GravitinoTable table = new GravitinoTable("db1", "table1", mockTable);
    Assertions.assertNull(table.getComment());

    CatalogConnectorMetadataAdapter adapter =
        new HiveMetadataAdapter(
            Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    ConnectorTableMetadata tableMetadata = adapter.getTableMetadata(table);
    Assertions.assertTrue(tableMetadata.getComment().isEmpty());
  }

  public static Table mockTable(
      String tableName, Column[] columns, String comment, Map<String, String> properties) {
    Table table = mock(Table.class);
    when(table.name()).thenReturn(tableName);
    when(table.columns()).thenReturn(columns);
    when(table.comment()).thenReturn(comment);
    when(table.properties()).thenReturn(properties);
    when(table.partitioning()).thenReturn(new Transform[0]);
    when(table.sortOrder()).thenReturn(new SortOrder[0]);
    when(table.distribution()).thenReturn(Distributions.NONE);

    Audit mockAudit = mock(Audit.class);
    when(mockAudit.creator()).thenReturn("gravitino");
    when(mockAudit.createTime()).thenReturn(Instant.now());
    when(table.auditInfo()).thenReturn(mockAudit);

    return table;
  }

  public static Table mockTable(
      String tableName,
      Column[] columns,
      String comment,
      Map<String, String> properties,
      Transform[] partitioning,
      SortOrder[] sortOrder,
      Distribution distribution) {
    Table table = mock(Table.class);
    when(table.name()).thenReturn(tableName);
    when(table.columns()).thenReturn(columns);
    when(table.comment()).thenReturn(comment);
    when(table.properties()).thenReturn(properties);
    when(table.partitioning()).thenReturn(partitioning);
    when(table.sortOrder()).thenReturn(sortOrder);
    when(table.distribution()).thenReturn(distribution);

    Audit mockAudit = mock(Audit.class);
    when(mockAudit.creator()).thenReturn("gravitino");
    when(mockAudit.createTime()).thenReturn(Instant.now());
    when(table.auditInfo()).thenReturn(mockAudit);

    return table;
  }
}
