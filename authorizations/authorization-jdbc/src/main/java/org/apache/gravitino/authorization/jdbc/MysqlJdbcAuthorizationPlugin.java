package org.apache.gravitino.authorization.jdbc;

import org.apache.gravitino.MetadataObject;
import org.apache.gravitino.authorization.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class MysqlJdbcAuthorizationPlugin extends JdbcAuthorizationPlugin {
  private static final Logger LOG = LoggerFactory.getLogger(MysqlJdbcAuthorizationPlugin.class);
  private static volatile MysqlJdbcAuthorizationPlugin instance = null;

  private MysqlJdbcAuthorizationPlugin(Map<String, String> config) {
    super(config);
  }

  public static synchronized MysqlJdbcAuthorizationPlugin getInstance(Map<String, String> config) {
    if (instance == null) {
      synchronized (MysqlJdbcAuthorizationPlugin.class) {
        if (instance == null) {
          instance = new MysqlJdbcAuthorizationPlugin(config);
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
