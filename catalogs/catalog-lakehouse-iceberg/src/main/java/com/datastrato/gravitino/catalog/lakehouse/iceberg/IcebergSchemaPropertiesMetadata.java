/*
 * Copyright 2023 Datastrato.
 * This software is licensed under the Apache License version 2.
 */
package com.datastrato.gravitino.catalog.lakehouse.iceberg;

import com.datastrato.gravitino.catalog.BasePropertiesMetadata;
import com.datastrato.gravitino.catalog.PropertyEntry;
import com.google.common.collect.Maps;
import java.util.Map;

public class IcebergSchemaPropertiesMetadata extends BasePropertiesMetadata {
  private static final Map<String, PropertyEntry<?>> propertiesMetadata = Maps.newHashMap();

  @Override
  protected Map<String, PropertyEntry<?>> specificPropertyEntries() {
    // TODO: support Iceberg schema properties metadata
    return propertiesMetadata;
  }
}
