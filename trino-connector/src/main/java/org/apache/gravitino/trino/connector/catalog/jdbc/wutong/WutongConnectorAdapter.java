/*
 * Copyright 2023 Datastrato Pvt Ltd.
 * This software is licensed under the Apache License version 2.
 */
package org.apache.gravitino.trino.connector.catalog.jdbc.wutong;

import org.apache.gravitino.catalog.property.PropertyConverter;
import org.apache.gravitino.trino.connector.catalog.CatalogConnectorAdapter;
import org.apache.gravitino.trino.connector.catalog.CatalogConnectorMetadataAdapter;
import org.apache.gravitino.trino.connector.catalog.jdbc.JDBCCatalogPropertyConverter;
import org.apache.gravitino.trino.connector.metadata.GravitinoCatalog;

import java.util.Map;

import static org.apache.gravitino.trino.connector.GravitinoConnectorPluginManager.CONNECTOR_WUTONG;
import static java.util.Collections.emptyList;

/** Transforming PostgreSQL connector configuration and components into Gravitino connector. */
public class WutongConnectorAdapter implements CatalogConnectorAdapter {
  private final PropertyConverter catalogConverter;

  public WutongConnectorAdapter() {
    this.catalogConverter = new JDBCCatalogPropertyConverter();
  }

  @Override
  public Map<String, String> buildInternalConnectorConfig(GravitinoCatalog catalog)
      throws Exception {
    return catalogConverter.gravitinoToEngineProperties(catalog.getProperties());
  }

  @Override
  public String internalConnectorName() {
    return CONNECTOR_WUTONG;
  }

  @Override
  public CatalogConnectorMetadataAdapter getMetadataAdapter() {
    // TODO yuhui Need to improve schema table and column properties
    return new WutongMetadataAdapter(getSchemaProperties(), getTableProperties(), emptyList());
  }
}
