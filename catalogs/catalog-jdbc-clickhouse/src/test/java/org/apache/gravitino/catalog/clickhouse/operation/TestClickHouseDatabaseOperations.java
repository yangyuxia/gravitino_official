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
package org.apache.gravitino.catalog.clickhouse.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.gravitino.catalog.jdbc.JdbcColumn;
import org.apache.gravitino.rel.expressions.NamedReference;
import org.apache.gravitino.rel.expressions.distributions.Distributions;
import org.apache.gravitino.rel.expressions.sorts.SortOrder;
import org.apache.gravitino.rel.expressions.sorts.SortOrders;
import org.apache.gravitino.rel.expressions.transforms.Transform;
import org.apache.gravitino.rel.indexes.Index;
import org.apache.gravitino.rel.types.Types;
import org.apache.gravitino.utils.RandomNameUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("gravitino-docker-test")
public class TestClickHouseDatabaseOperations extends TestClickHouse {

  @Test
  public void testBaseOperationDatabase() {
    String databaseName = RandomNameUtils.genRandomName("ct_db");
    Map<String, String> properties = new HashMap<>();
    // Clickhouse database creation does not support incoming comments.
    String comment = null;
    List<String> databases = DATABASE_OPERATIONS.listDatabases();
    ((ClickHouseDatabaseOperations) DATABASE_OPERATIONS)
        .createSysDatabaseNameSet()
        .forEach(
            sysClickhouseDatabaseName ->
                Assertions.assertFalse(databases.contains(sysClickhouseDatabaseName)));
    testBaseOperation(databaseName, properties, comment);
    testDropDatabase(databaseName);
  }

  @Test
  void testDropTableWithSpecificName() {
    String databaseName = RandomNameUtils.genRandomName("ct_db") + "-abc-" + "end";
    Map<String, String> properties = new HashMap<>();
    DATABASE_OPERATIONS.create(databaseName, null, properties);
    DATABASE_OPERATIONS.delete(databaseName, false);

    databaseName = RandomNameUtils.genRandomName("ct_db") + "_end";
    DATABASE_OPERATIONS.create(databaseName, null, properties);

    String tableName = RandomStringUtils.randomAlphabetic(16) + "_op_table";
    String tableComment = "test_comment";
    List<JdbcColumn> columns = new ArrayList<>();
    columns.add(
        JdbcColumn.builder()
            .withName("col_1")
            .withType(Types.VarCharType.of(100))
            .withComment("test_comment")
            .withNullable(false)
            .build());
    columns.add(
        JdbcColumn.builder()
            .withName("col_2")
            .withType(Types.VarCharType.of(100))
            .withComment("test_comment2")
            .withNullable(true)
            .build());

    Index[] indexes = new Index[] {};

    SortOrder[] sortOrders =
        new SortOrders.SortImpl[] {SortOrders.of(NamedReference.field("col_1"), null, null)};

    TABLE_OPERATIONS.create(
        databaseName,
        tableName,
        columns.toArray(new JdbcColumn[0]),
        tableComment,
        properties,
        new Transform[0],
        Distributions.NONE,
        indexes,
        sortOrders);
    DATABASE_OPERATIONS.delete(databaseName, true);
  }
}
