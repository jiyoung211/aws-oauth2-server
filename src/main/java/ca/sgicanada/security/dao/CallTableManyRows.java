package ca.sgicanada.security.dao ;

import java.math.BigDecimal ;
import java.sql.Connection ;
import java.sql.ResultSet ;
import java.sql.ResultSetMetaData ;
import java.sql.SQLException ;
import java.sql.Timestamp ;
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.Set ;

import javax.naming.Context ;
import javax.naming.InitialContext ;
import javax.naming.NamingException ;
import javax.sql.DataSource ;

import org.adeptnet.sql.NamedParameterStatement ;

import ca.sgicanada.dao.CallProcManyRows ;

public class CallTableManyRows extends CallProcManyRows
{
  // private static Logger log = Logger.getLogger(CallProcManyRows.class) ;
  String jcmCacheString = "SybaseDataSource" ;
  String jndiNamespace = "java:comp/env" ;
  boolean useFullJndiName = false ;

  // ArrayList<HashMap<String, Object>> resultSet = null ;
  // resultSet = spSelectDAO.callProc("s_oauth_accesstoken", null,
  // resourceOwnerSubject != null ? resourceOwnerSubject.getLogin() : null,
  // c.getClientId()) ;

  public CallTableManyRows(String cacheName)
  {
    super(cacheName) ;
    this.jcmCacheString = cacheName ;
  }

  public CallTableManyRows(String cacheName, boolean useFullJndiName)
  {
    super(cacheName, useFullJndiName) ;

    this.jcmCacheString = cacheName ;
    this.useFullJndiName = useFullJndiName ;
  }

  public CallTableManyRows(String cacheName, String jndiNamespace)
  {
    super(cacheName, jndiNamespace) ;
    this.jcmCacheString = cacheName ;
    this.jndiNamespace = jndiNamespace ;
  }

  @Override
  public ArrayList<HashMap<String, Object>> callProc(String procNm, Object... args) throws SQLException
  {
    System.out.println("callProc") ;
    // TODO Auto-generated method stub
    // super.callProc(procNm, args) ;
    return callTable(procNm, args) ;
  }

  public ArrayList<HashMap<String, Object>> callTable(String procNm, Object... args) throws SQLException
  {
    System.out.println("callTable") ;
    ArrayList<HashMap<String, Object>> rsMany = new ArrayList() ;
    new HashMap() ;
    Connection dbConn = null ;
    boolean dbError = false ;
    String dbMessage = "" ;
    int idx = -1 ;
    String questionMarks = "" ;
    String callString = "" ;
    String colName = "" ;
    String rowString = "" ;
    byte[] rowBytes = null ;
    Integer rowInteger = 0 ;
    Long rowLong = 0L ;
    new BigDecimal(0) ;
    Timestamp rowTimeStamp = null ;
    Boolean rowBoolean = false ;
    String debugProcCall = "" ;

    ArrayList var31 ;
    try
    {
      Context initCtx = new InitialContext() ;
      DataSource ds = null ;
      Context envCtx ;
      boolean useFullJndiName ;
      if (this.useFullJndiName)
      {
        ds = (DataSource) initCtx.lookup(this.jcmCacheString) ;
      }
      else
      {
        envCtx = (Context) initCtx.lookup(this.jndiNamespace) ;
        ds = (DataSource) envCtx.lookup("jdbc/" + this.jcmCacheString) ;
      }

      dbConn = ds.getConnection() ;
      if (dbConn == null)
      {
        System.out.println(this.getClass() + " could not retrieve connection.") ;
        throw new SQLException() ;
      }

      // String sql = procNmToSql(dbConn, procNm, args) ;//
      // "select * from BearerAccessToken"
      // PreparedStatement pstmt = dbConn.prepareStatement(sql) ;
      NamedParameterStatement nstmt = createNamedParameterStatement(dbConn, procNm, args) ;
      // PreparedStatement pstmt = createPreparedStatement(dbConn, procNm, args)
      // ;

      System.out.println("nstmt : " + nstmt.toString()) ;
      ResultSet rs = nstmt.executeQuery() ;

      boolean found = rs.next() ;
      if (found)
      {
        ResultSetMetaData metaData = rs.getMetaData() ;
        int colCount = metaData.getColumnCount() ;

        do
        {
          rsMany.add(new HashMap()) ;
          ++idx ;
          HashMap<String, Object> rowHashMap = (HashMap) rsMany.get(idx) ;
          int i ;
          for (i = 1 ; i <= colCount ; ++i)
          {
            colName = metaData.getColumnLabel(i) ;
            if (colName == null || colName.equals(""))
            {
              colName = "col" + Integer.toString(i) ;
            }

            if (metaData.getColumnType(i) != -1 && metaData.getColumnType(i) != 1 && metaData.getColumnType(i) != 12)
            {
              if (metaData.getColumnType(i) != 4 && metaData.getColumnType(i) != 5 && metaData.getColumnType(i) != -6)
              {
                if (metaData.getColumnType(i) == -5)
                {
                  rowLong = rs.getLong(i) ;
                  if (rs.wasNull())
                  {
                    rowLong = null ;
                  }

                  rowHashMap.put(colName, rowLong) ;
                }
                else if (metaData.getColumnType(i) != 3 && metaData.getColumnType(i) != 2)
                {
                  if (metaData.getColumnType(i) == 93)
                  {
                    rowTimeStamp = rs.getTimestamp(i) ;
                    if (rs.wasNull())
                    {
                      rowTimeStamp = null ;
                    }

                    rowHashMap.put(colName, rowTimeStamp) ;
                  }
                  else if (metaData.getColumnType(i) == -7)
                  {
                    rowBoolean = rs.getBoolean(i) ;
                    if (rs.wasNull())
                    {
                      rowBoolean = null ;
                    }

                    rowHashMap.put(colName, rowBoolean) ;
                  }
                  else
                  {
                    if (metaData.getColumnType(i) != -4)
                    {
                      throw new IllegalArgumentException("Invalid datatype returned from database: " + metaData.getColumnTypeName(i)) ;
                    }

                    byte[] rowBytes1 = rs.getBytes(i) ;
                    if (rs.wasNull())
                    {
                      rowBytes1 = null ;
                    }

                    rowHashMap.put(colName, rowBytes1) ;
                  }
                }
                else
                {
                  BigDecimal rowBigDecimal = rs.getBigDecimal(i) ;
                  long rowlongValue = 0 ;
                  if (rs.wasNull())
                  {
                    rowBigDecimal = null ;
                  }
                  else
                  {
                    rowlongValue = rowBigDecimal.longValue() ;
                  }
                  rowHashMap.put(colName, rowlongValue) ;
                }
              }
              else
              {
                rowInteger = rs.getInt(i) ;
                if (rs.wasNull())
                {
                  rowInteger = null ;
                }

                rowHashMap.put(colName, rowInteger) ;
              }
            }
            else
            {
              rowString = rs.getString(i) ;
              if (rs.wasNull())
              {
                rowString = null ;
              }

              rowHashMap.put(colName, rowString) ;
            }
          }

          rsMany.set(idx, rowHashMap) ;
        } while (rs.next()) ;

        if (rs != null)
        {
          rs.close() ;
        }

        if (nstmt != null)
        {
          nstmt.close() ;
          envCtx = null ;
        }
        ArrayList<HashMap<String, Object>> callResult = toLowerCaseColumnName(rsMany) ;
        return callResult ;
      }

      if (rs != null)
      {
        rs.close() ;
      }

      if (nstmt != null)
      {
        nstmt.close() ;
        envCtx = null ;
      }

      if (dbConn != null)
      {
        dbConn.close() ;
      }

      dbConn = null ;
      var31 = rsMany ;
    }
    catch (NamingException var41)
    {
      System.out.println(var41.getMessage()) ;
      // log.error(var41.getMessage()) ;
      throw new SQLException(var41) ;
    }
    catch (SQLException var42)
    {
      dbMessage = var42.getErrorCode() + " CallProcManyRows:" + debugProcCall + " SQLException " + var42.getMessage() ;
      dbError = true ;
      throw var42 ;
    }
    catch (Exception var43)
    {
      dbMessage = "Unknown Exception " + this.getClass().getName() + ": " + debugProcCall + var43.getMessage() ;
      dbError = true ;
      throw new RuntimeException(dbMessage, var43) ;
    }
    finally
    {
      if (dbConn != null)
      {
        try
        {
          dbConn.close() ;
          dbConn = null ;
        }
        catch (Exception var40)
        {
          System.out.println("Connection close " + var40.getMessage()) ;
        }
      }

      if (dbError)
      {
        System.out.println(dbMessage) ;
        // log.error(dbMessage) ;
      }

    }
    ArrayList<HashMap<String, Object>> callResult = toLowerCaseColumnName(var31) ;

    return callResult ;
  }

  public ArrayList<HashMap<String, Object>> toLowerCaseColumnName(ArrayList<HashMap<String, Object>> upperMap)
  {
    ArrayList<HashMap<String, Object>> lowerList = new ArrayList<HashMap<String, Object>>() ;
    for (HashMap<String, Object> map : upperMap)
    {
      Set<String> keySet = map.keySet() ;
      Iterator<String> it = keySet.iterator() ;
      HashMap<String, Object> lowerMap = new HashMap<String, Object>() ;
      while (it.hasNext())
      {
        String key = it.next() ;
        lowerMap.put(key.toLowerCase(), map.get(key)) ;
      }
      lowerList.add(lowerMap) ;
    }
    return lowerList ;
  }

  private NamedParameterStatement createNamedParameterStatement(Connection dbConn, String procNm, Object... args)
  {
    System.out.println("procNm [" + procNm + "]") ;
    String sql = new String() ;
    StringBuilder sb = new StringBuilder() ;
    NamedParameterStatement nstmt = null ;

    switch (procNm)
    {
    case "s_oauth_accesstoken_parm" :
      // spSelectDAO.callProc("s_oauth_accesstoken_parm",
      // serverAccessToken.getTokenKey()) ;
    case "s_oauth_accesstoken_oauthperm" :
      // spSelectDAO.callProc("s_oauth_accesstoken_oauthperm",
      // serverAccessToken.getTokenKey()) ;
    case "s_oauth_accesstoken_extraprop" :
      // spSelectDAO.callProc("s_oauth_accesstoken_extraprop",
      // serverAccessToken.getTokenKey()) ;
    case "s_oauth_accesstoken_audiences" :
      // spSelectDAO.callProc("s_oauth_accesstoken_audiences",
      // serverAccessToken.getTokenKey()) ;

      sb.append(" SELECT t.* FROM BearerAccessToken t ") ;
      sb.append(" WHERE t.tokenkey =  :tokenkey               ") ; // 'null'

      try
      {
        nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
        nstmt.setString("tokenkey", (String) args[0]) ;
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
      }

      break ;
    // 1
    case "s_oauth_accesstoken" :
    case "s_access_token" :
      // spSelectDAO.callProc("s_oauth_accesstoken", null, resourceOwnerSubject
      // != null ? resourceOwnerSubject.getLogin() : null, c.getClientId()) ;
      sb.append(" SELECT t.* FROM BearerAccessToken t ") ;
      sb.append(" join usersubject s                  ") ;
      sb.append(" on t.SUBJECT_ID = s.id              ") ;
      sb.append(" JOIN client c                       ") ;
      sb.append(" ON t.CLIENT_CLIENTID = c.CLIENTID   ") ;
      sb.append(" WHERE t.tokenkey =  :tokenkey               ") ; // 'null'
      sb.append(" AND   c.clientId = :clientId               ") ;
      sb.append(" AND    s.login = :login  ;               ") ;

      try
      {
        nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
        nstmt.setString("tokenkey", (String) args[0]) ;
        nstmt.setString("login", (String) args[1]) ;
        nstmt.setString("clientId", (String) args[2]) ;
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
      }

      break ;

    // 2
    case "s_oauth_refreshtoken_parm" :
      // spSelectDAO.callProc("s_oauth_refreshtoken_parm",
      // refreshToken.getTokenKey()) ;
    case "s_oauth_refreshtoken_oauthperm" :
      // spSelectDAO.callProc("s_oauth_refreshtoken_oauthperm",
      // refreshToken.getTokenKey()) ;
    case "s_oauth_refreshtoken_extraprop" :
      // spSelectDAO.callProc("s_oauth_refreshtoken_extraprop",
      // refreshToken.getTokenKey()) ;
    case "s_oauth_refreshtoken_audiences" :
      // spSelectDAO.callProc("s_oauth_refreshtoken_audiences",
      // refreshToken.getTokenKey()) ;
    case "s_oauth_refreshtoken_access" :
      // spSelectDAO.callProc("s_oauth_refreshtoken_access",
      // refreshToken.getTokenKey()) ;
      sb.append(" SELECT t.* FROM RefreshToken t      ") ;
      sb.append(" WHERE  t.refreshtoken = :refreshtoken          ") ;
      try
      {
        nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
        nstmt.setString("refreshtoken", (String) args[0]) ;
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
      }

      break ;

    case "s_oauth_refreshtoken" :
      // spSelectDAO.callProc("s_oauth_refreshtoken", null, resourceOwnerSubject
      // != null ? resourceOwnerSubject.getLogin() : null, c.getClientId()) ;
      // spSelectDAO.callProc("s_oauth_refreshtoken", refreshTokenKey, null,
      // null) ;
    case "s_refresh_token" :
      // refreshtoken,clientid, login
      sb.append(" SELECT t.* FROM RefreshToken t      ") ;
      sb.append(" JOIN usersubject s                  ") ;
      sb.append(" ON t.SUBJECT_ID = s.ID              ") ;
      sb.append(" JOIN client c                       ") ;
      sb.append(" ON t.CLIENT_CLIENTID = c.CLIENTID   ") ;
      sb.append(" WHERE  t.refreshtoken = :refreshtoken          ") ;
      sb.append(" AND s.login = :login                      ") ;
      sb.append(" AND c.clientId = :clientId  ;                ") ;

      try
      {
        nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
        nstmt.setString("refreshtoken", (String) args[0]) ;
        nstmt.setString("login", (String) args[1]) ;
        nstmt.setString("clientId", (String) args[2]) ;
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
      }

      break ;

    // 3
    case "o_access_token" :

      break ;
    // 4
    case "o_refresh_token" :
      break ;
    // 5
    case "d_access_token" :
      break ;
    // 6
    case "d_refresh_token" :
      break ;
    // 7
    case "u_access_token" :
      break ;

    case "s_oauth_serverauthcodegrant_extr_prop" :
      // spSelectDAO.callProc("s_oauth_serverauthcodegrant_extr_prop",
      // serverAuthorizationCodeGrant.getCode()) ;
    case "s_oauth_serverauthcodegrant_req_scopes" :
      // spSelectDAO.callProc("s_oauth_serverauthcodegrant_req_scopes",
      // serverAuthorizationCodeGrant.getCode()) ;
    case "s_oauth_serverauthcodegrant_aprv_scopes" :
      // spSelectDAO.callProc("s_oauth_serverauthcodegrant_aprv_scopes",
      // serverAuthorizationCodeGrant.getCode()) ;
    case "s_oauth_serverauthcodegrant" :
      // spSelectDAO.callProc("s_oauth_serverauthcodegrant", c.getClientId(),
      // resourceOwnerSubject != null ? resourceOwnerSubject.getId() : null) ;
      // spSelectDAO.callProc("s_oauth_serverauthcodegrant", code, null, null) ;
      if (args.length == 2)
      {
        // resultSet = spSelectDAO.callProc("s_oauth_serverauthcodegrant",
        // c.getClientId(),
        // resourceOwnerSubject != null ? resourceOwnerSubject.getId() : null) ;

        sb.append(" SELECT * FROM serverauthorizationcodegrant       ") ;
        sb.append(" WHERE  client_clientid = :client_clientid          ") ;
        sb.append(" AND  subject_id = :subject_id          ") ;

        try
        {
          nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
          nstmt.setString("client_clientid", (String) args[0]) ;
          nstmt.setString("subject_id", (String) args[1]) ;

        }
        catch (SQLException e)
        {
          e.printStackTrace() ;
        }

      }
      else
      {
        // resultSet = spSelectDAO.callProc("s_oauth_serverauthcodegrant",
        // code,
        // null,
        // null) ;
        sb.append(" SELECT * FROM serverauthorizationcodegrant       ") ;
        sb.append(" WHERE  code = :code          ") ;

        try
        {
          nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
          nstmt.setString("code", (String) args[0]) ;

        }
        catch (SQLException e)
        {
          e.printStackTrace() ;
        }

      }

      break ;

    case "s_oauth_sessionauthenticitytoken" :
      // spSelectDAO.callProc("s_oauth_sessionauthenticitytoken",
      // sessionAuthToken) ;
      sb.append(" SELECT * FROM sessionauthenticitytoken       ") ;
      sb.append(" WHERE  sessionauthtoken = :sessionauthtoken          ") ;

      try
      {
        nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
        nstmt.setString("sessionauthtoken", (String) args[0]) ;
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
      }

    case "s_oauth_permission_uris" :
      // spSelectDAO.callProc("s_oauth_permission_uris",
      // oauthPermission.getPermission()) ;
    case "s_oauth_permission_httpverbs" :
      // spSelectDAO.callProc("s_oauth_permission_httpverbs",
      // oauthPermission.getPermission()) ;
    case "s_oauth_permission" :
      // spSelectDAO.callProc("s_oauth_permission", permission) ;
      break ;

    case "s_oauth_usersubject_roles" :
      // spSelectDAO.callProc("s_oauth_usersubject_roles", userSubject.getId())
      // ;

    case "s_oauth_usersubject" :
      // spSelectDAO.callProc("s_oauth_usersubject", subjectId, login) ;
      sb.append(" SELECT * FROM USERSUBJECT       ") ;
      sb.append(" WHERE  id = :id          ") ;
      sb.append(" AND login = :login                      ") ;

      try
      {
        nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
        nstmt.setString("id", (String) args[0]) ;
        nstmt.setString("login", (String) args[1]) ;
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
      }

      break ;

    case "s_oauth_client" :
      // resultSet = spSelectDAO.callProc("s_oauth_client", clientId) ;
      sb.append(" SELECT * FROM CLIENT       ") ;
      sb.append(" WHERE  clientid = :clientid          ") ;

      try
      {
        nstmt = new NamedParameterStatement(dbConn, sb.toString()) ;
        nstmt.setString("clientid", (String) args[0]) ;
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
      }

      break ;

    default :
      throw new UnsupportedOperationException("[" + procNm + "]") ;
    }

    return nstmt ;
  }

  // private PreparedStatement createPreparedStatement(Connection dbConn, String
  // procNm, Object... args)
  // {
  // String sql = new String() ;
  // StringBuilder sb = new StringBuilder() ;
  // PreparedStatement pstmt = null ;
  //
  // switch (procNm)
  // {
  // // 1
  // case "s_access_token" :
  // case "s_oauth_accesstoken" : // s_access_token" :
  // // tokenkey,clientid, login
  // sb.append(" SELECT t.* FROM BearerAccessToken t ") ;
  // sb.append(" join usersubject s                  ") ;
  // sb.append(" on t.SUBJECT_ID = s.id              ") ;
  // sb.append(" JOIN client c                       ") ;
  // sb.append(" ON t.CLIENT_CLIENTID = c.CLIENTID   ") ;
  // sb.append(" WHERE t.tokenkey =  ?              ") ; // 'null'
  // sb.append(" AND   c.clientId = ?               ") ;
  // sb.append(" AND    s.login = ?  ;               ") ;
  //
  // try
  // {
  // pstmt = dbConn.prepareStatement(sb.toString()) ;
  // pstmt.setString(1, (String) args[0]) ;
  // pstmt.setString(2, (String) args[1]) ;
  // pstmt.setString(3, (String) args[2]) ;
  // }
  // catch (SQLException e)
  // {
  // e.printStackTrace() ;
  // }
  //
  // break ;
  //
  // // 2
  // case "s_refresh_token" :
  // // refreshtoken,clientid, login
  // sb.append(" SELECT t.* FROM RefreshToken t      ") ;
  // sb.append(" JOIN usersubject s                  ") ;
  // sb.append(" ON t.SUBJECT_ID = s.ID              ") ;
  // sb.append(" JOIN client c                       ") ;
  // sb.append(" ON t.CLIENT_CLIENTID = c.CLIENTID   ") ;
  // sb.append(" WHERE  t.refreshtoken = ?           ") ;
  // sb.append(" AND s.login = ?                     ") ;
  // sb.append(" AND c.clientId = ? ;                ") ;
  // break ;
  //
  // // 3
  // case "o_access_token" :
  //
  // break ;
  // // 4
  // case "o_refresh_token" :
  // break ;
  // // 5
  // case "d_access_token" :
  // break ;
  // // 6
  // case "d_refresh_token" :
  // break ;
  // // 7
  // case "u_access_token" :
  // break ;
  //
  // default :
  // throw new UnsupportedOperationException() ;
  // }
  //
  // return pstmt ;
  // }

}
