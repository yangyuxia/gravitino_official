/*
 * Copyright 2023 Datastrato Pvt Ltd.
 * This software is licensed under the Apache License version 2.
 */
package org.apache.gravitino.trino.connector.catalog.jdbc.wutong;

import org.apache.gravitino.trino.connector.catalog.CatalogConnectorMetadataAdapter;
import io.trino.spi.session.PropertyMetadata;

import java.util.List;

/** Transforming gravitino PostgreSQL metadata to trino. */
public class WutongMetadataAdapter extends CatalogConnectorMetadataAdapter {

  public WutongMetadataAdapter(
      List<PropertyMetadata<?>> schemaProperties,
      List<PropertyMetadata<?>> tableProperties,
      List<PropertyMetadata<?>> columnProperties) {

    super(schemaProperties, tableProperties, columnProperties, new WutongDataTypeTransformer());
  }
}
