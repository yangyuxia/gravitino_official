package org.apache.gravitino.authorization.jdbc.postgresql;

import java.util.Map;
import org.apache.gravitino.connector.authorization.AuthorizationPlugin;
import org.apache.gravitino.connector.authorization.BaseAuthorization;

public class PostgresqlAuthorization extends BaseAuthorization<PostgresqlAuthorization> {
  public PostgresqlAuthorization() {}

  @Override
  public String shortName() {
    return "postgresql";
  }

  @Override
  public AuthorizationPlugin newPlugin(String metalake, String catalogProvider, Map<String, String> config) {
    return PostgresqlJdbcAuthorizationPlugin.getInstance(config);
  }


}
