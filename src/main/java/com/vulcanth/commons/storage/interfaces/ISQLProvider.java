package org.nebula.core.storage.interfaces;

import javax.sql.rowset.CachedRowSet;

public interface ISQLProvider {

  void connect();

  void disconnect();

  void execute(String query, Object... params);

  void update(String query, Object... params);

  int updateReturnId(String query, Object... params);

  CachedRowSet query(String query, Object... params);
}
