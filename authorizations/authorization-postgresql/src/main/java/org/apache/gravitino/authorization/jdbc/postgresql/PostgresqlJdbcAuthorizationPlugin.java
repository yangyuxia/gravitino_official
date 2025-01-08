package org.apache.gravitino.authorization.jdbc.postgresql;

import org.apache.gravitino.MetadataObject;
import org.apache.gravitino.authorization.Owner;
import org.apache.gravitino.authorization.jdbc.JdbcAuthorizationPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class PostgresqlJdbcAuthorizationPlugin extends JdbcAuthorizationPlugin {
  private static final Logger LOG = LoggerFactory.getLogger(PostgresqlJdbcAuthorizationPlugin.class);
  private static volatile PostgresqlJdbcAuthorizationPlugin instance = null;

  private PostgresqlJdbcAuthorizationPlugin(Map<String, String> config) {
    super(config);
  }

  public static synchronized PostgresqlJdbcAuthorizationPlugin getInstance(Map<String, String> config) {
    if (instance == null) {
      synchronized (PostgresqlJdbcAuthorizationPlugin.class) {
        if (instance == null) {
          instance = new PostgresqlJdbcAuthorizationPlugin(config);
        }
      }
    }
    return instance;
  }

  @Override
  public List<String> getSetOwnerSQL(MetadataObject.Type type, String objectName, Owner preOwner, Owner newOwner) {
    return null;
  }
}
