package org.dbos.apiary.microbench.functions;

import org.dbos.apiary.postgres.PostgresContext;
import org.dbos.apiary.postgres.PostgresFunction;

import java.sql.SQLException;

public class NectarCreate extends PostgresFunction {
    public static final String insertKey = "INSERT INTO ObjectStore (ObjectId, Key, Val) VALUES (?, ?, ?);";
    public static int runFunction(PostgresContext ctxt, int objectId, int numEntries, int entrySize) throws SQLException {
      String value = new String(new char[entrySize]).replace('\0', 'x');
      for ( int i = 0; i < numEntries; i++ ) {
        ctxt.executeUpdate(insertKey, objectId, i, value);
      }
      return 0;
    }
}