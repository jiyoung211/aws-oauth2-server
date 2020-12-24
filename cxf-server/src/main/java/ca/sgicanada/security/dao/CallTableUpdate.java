package ca.sgicanada.security.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.adeptnet.sql.NamedParameterStatement;

import ca.sgicanada.dao.CallProcUpdate;

public class CallTableUpdate extends CallProcUpdate
{
	// private static Logger log = Logger.getLogger(CallProcManyRows.class) ;
	String jcmCacheString = "SybaseDataSource";
	String jndiNamespace = "java:comp/env";
	boolean useFullJndiName = false;

	// ArrayList<HashMap<String, Object>> resultSet = null ;
	// resultSet = spSelectDAO.callProc("s_oauth_accesstoken", null,
	// resourceOwnerSubject != null ? resourceOwnerSubject.getLogin() : null,
	// c.getClientId()) ;

	public CallTableUpdate(String cacheName)
	{
		super(cacheName);
		this.jcmCacheString = cacheName;
	}

	public CallTableUpdate(String cacheName, boolean useFullJndiName)
	{
		super(cacheName, useFullJndiName);

		this.jcmCacheString = cacheName;
		this.useFullJndiName = useFullJndiName;
	}

	public CallTableUpdate(String cacheName, String jndiNamespace)
	{
		super(cacheName, jndiNamespace);
		this.jcmCacheString = cacheName;
		this.jndiNamespace = jndiNamespace;
	}

	@Override
	public void callUpdateProc(String procNm, Object... args) throws SQLException
	{
		System.out.println("callUpdateProc");
		callUpdateTable(procNm, args);
		return;
	}

	public void callUpdateTable(String procNm, Object... args) throws SQLException
	{
		Connection dbConn = null;
		boolean dbError = false;
		String dbMessage = "";
		String questionMarks = "";
		String callString = "";
		String debugProcCall = "";

		try
		{
			Context initCtx = new InitialContext();
			DataSource ds = null;
			Context envCtx;
			if (this.useFullJndiName)
			{
				ds = (DataSource) initCtx.lookup(this.jcmCacheString);
			} else
			{
				envCtx = (Context) initCtx.lookup(this.jndiNamespace);
				ds = (DataSource) envCtx.lookup("jdbc/" + this.jcmCacheString);
			}

			dbConn = ds.getConnection();
			if (dbConn == null)
			{
				System.out.println(this.getClass() + " could not retrieve connection.");
				// log.error(this.getClass() +
				// " could not retrieve connection.") ;
				throw new SQLException();
			}
			// CallableStatement cstmt = dbConn.prepareCall(callString);
			NamedParameterStatement nstmt = createNamedParameterStatement(dbConn, procNm, args);
			int i = nstmt.executeUpdate();
			if (nstmt != null)
			{
				nstmt.close();
				envCtx = null;
			}
		} catch (NamingException var26)
		{
			dbMessage = "NamingException occurred: " + var26.getMessage();
			dbError = true;
			throw new SQLException(var26);
		} catch (SQLException var27)
		{
			if (var27.getErrorCode() != 20100 && var27.getErrorCode() != 20050)
			{
				dbMessage = var27.getErrorCode() + " CallProcUpdate:" + debugProcCall + " SQLException "
						+ var27.getMessage();
				dbError = true;
			}

			throw var27;
		} catch (Exception var28)
		{
			dbMessage = "Unknown Exception " + this.getClass().getName() + ": " + debugProcCall + var28.getMessage();
			dbError = true;
			throw new RuntimeException(dbMessage, var28);
		} finally
		{
			if (dbConn != null)
			{
				try
				{
					dbConn.close();
					dbConn = null;
				} catch (Exception var25)
				{
					System.out.println("Connection close " + var25.getMessage());
				}
			}

			if (dbError)
			{
				System.out.println(dbMessage);
				// log.error(dbMessage);
			}

		}
	}

	public ArrayList<HashMap<String, Object>> toLowerCaseColumnName(ArrayList<HashMap<String, Object>> upperMap)
	{
		ArrayList<HashMap<String, Object>> lowerList = new ArrayList<HashMap<String, Object>>();
		for (HashMap<String, Object> map : upperMap)
		{
			Set<String> keySet = map.keySet();
			Iterator<String> it = keySet.iterator();
			HashMap<String, Object> lowerMap = new HashMap<String, Object>();
			while (it.hasNext())
			{
				String key = it.next();
				lowerMap.put(key.toLowerCase(), map.get(key));
			}
			lowerList.add(lowerMap);
		}
		return lowerList;
	}

	private NamedParameterStatement createNamedParameterStatement(Connection dbConn, String procNm, Object... args)
	{
		System.out.println("procNm [" + procNm + "]");
		String sql = new String();
		StringBuilder sb = new StringBuilder();
		NamedParameterStatement nstmt = null;

		switch (procNm)
		{
		case "i_oauth_sessionauthenticitytoken":
			// spUpdateDAO.callUpdateProc("i_oauth_sessionauthenticitytoken",
			// sessionAuthToken, expiresin, clientId, userSubjectId) ;
			sb.append("    INSERT INTO sessionauthenticitytoken");
			sb.append("     VALUES                               ");
			sb.append("     (                      `              ");
			sb.append("     :sessionauthtoken ,                    ");
			sb.append("     :expiresin  ,                         ");
			sb.append("     :client_clientid  ,                   ");
			sb.append("     :subject_id                          ");
			sb.append("     )                                   ");

			try
			{
				System.out.println("execute query : " + sb.toString());
				System.out.println("args : " + Arrays.toString(args));
				nstmt = new NamedParameterStatement(dbConn, sb.toString());
				nstmt.setString("sessionauthtoken", (String) args[0]);
				nstmt.setLong("expiresin", (Long) args[1]);
				nstmt.setString("client_clientid", (String) args[2]);
				nstmt.setString("subject_id", (String) args[3]);

			} catch (SQLException e)
			{
				e.printStackTrace();
			}

			break;

		case "i_oauth_usersubject":
			// userSubject.getId(), userSubject.getAuthenticationMethod() !=
			// null ?
			// userSubject.getAuthenticationMethod().ordinal() : 0,
			// userSubject.getLogin()
			sb.append("    INSERT INTO usersubject");
			sb.append("     VALUES                               ");
			sb.append("     (                                    ");
			sb.append("     :id,                    ");
			sb.append("     :authenticationmethod  ,                         ");
			sb.append("     :login                     ");
			sb.append("     )                                   ");

			try
			{
				System.out.println("execute query : " + sb.toString());
				System.out.println("args : " + Arrays.toString(args));
				nstmt = new NamedParameterStatement(dbConn, sb.toString());
				nstmt.setString("id", (String) args[0]);
				nstmt.setInt("authenticationmethod", (Integer) args[1]);
				nstmt.setString("login", (String) args[2]);

			} catch (SQLException e)
			{
				e.printStackTrace();
			}

			break;

		case "i_oauth_serverauthcodegrant":
			// spUpdateDAO.callUpdateProc("i_oauth_serverauthcodegrant",
			// serverAuthorizationCodeGrant.getCode(),
			// serverAuthorizationCodeGrant.getCodeVerifier(),
			// serverAuthorizationCodeGrant.getRedirectUri(),
			// serverAuthorizationCodeGrant.getAudience(),
			// serverAuthorizationCodeGrant.getClientCodeChallenge(),
			// serverAuthorizationCodeGrant.getExpiresIn(),
			// serverAuthorizationCodeGrant.getIssuedAt(),
			// serverAuthorizationCodeGrant.getNonce(),
			// serverAuthorizationCodeGrant.isPreauthorizedTokenAvailable(),
			// serverAuthorizationCodeGrant.getResponseType(),
			// serverAuthorizationCodeGrant.getClient().getClientId(),
			// serverAuthorizationCodeGrant.getSubject() != null ?
			// serverAuthorizationCodeGrant.getSubject().getId() : null) ;

			sb.append("    INSERT INTO SERVERAUTHORIZATIONCODEGRANT");
			sb.append("     VALUES                               ");
			sb.append("     (                                    ");
			sb.append("      :code  ,                              ");
			sb.append("     :codeverifier  ,                        ");
			sb.append("     :redirecturi ,                         ");
			sb.append("    :audience ,                             ");
			sb.append("    :clientcodechallenge ,                  ");
			sb.append("    :expiresin  ,                           ");
			sb.append("    :issuedat ,                            ");
			sb.append("   :nonce   ,                              ");
			sb.append("    :preauthorizedtokenavailable  ,         ");
			sb.append("    :responsetype   ,                       ");
			sb.append("   :client_clientid  ,                       ");
			sb.append("    :subject_id                              ");
			sb.append("     )                                   ");

			try
			{
				System.out.println("execute query : " + sb.toString());
				System.out.println("args : " + Arrays.toString(args));
				nstmt = new NamedParameterStatement(dbConn, sb.toString());
				nstmt.setString("code", (String) args[0]);
				nstmt.setString("codeverifier", (String) args[1]);
				nstmt.setString("redirecturi", (String) args[2]);
				nstmt.setString("audience", (String) args[3]);
				nstmt.setString("clientcodechallenge", (String) args[4]);
				nstmt.setLong("expiresin", (Long) args[5]);
				nstmt.setLong("issuedat", (Long) args[6]);
				nstmt.setString("nonce", (String) args[7]);
				if ((Boolean) args[8])
				{
					nstmt.setString("preauthorizedtokenavailable", "1");
				} else
				{
					nstmt.setString("preauthorizedtokenavailable", "0");
				}
				nstmt.setString("responsetype", (String) args[9]);
				nstmt.setString("client_clientid", (String) args[10]);
				nstmt.setString("subject_id", (String) args[11]);

			} catch (SQLException e)
			{
				e.printStackTrace();
			}

			break;

		case "d_oauth_serverauth":
			// spUpdateDAO.callUpdateProc("d_oauth_serverauth", codeGrant) ;

			sb.append("    delete from SERVERAUTHORIZATIONCODEGRANT ");
			sb.append("     where code = :code                                ");

			try
			{
				System.out.println("execute query : " + sb.toString());
				System.out.println("args : " + Arrays.toString(args));
				nstmt = new NamedParameterStatement(dbConn, sb.toString());
				nstmt.setString("code", (String) args[0]);

			} catch (SQLException e)
			{
				e.printStackTrace();
			}

			break;

		case "i_oauth_accesstoken":
			// serverAccessToken.getTokenKey(),
			// serverAccessToken.getExpiresIn(),
			// serverAccessToken.getIssuedAt(),
			// serverAccessToken.getIssuer(),
			// serverAccessToken.getRefreshToken(),
			// serverAccessToken.getTokenType(),
			// serverAccessToken.getClientCodeVerifier(),
			// serverAccessToken.getGrantCode(),
			// serverAccessToken.getGrantType(),
			// serverAccessToken.getNonce(),
			// serverAccessToken.getResponseType(),
			// serverAccessToken.getClient() != null
			// ?serverAccessToken.getClient().getClientId() : null,
			// serverAccessToken.getSubject() != null
			// ?serverAccessToken.getSubject().getId() : null

			sb.append("    INSERT INTO BEARERACCESSTOKEN");
			sb.append("     VALUES                               ");
			sb.append("     (                                    ");
			sb.append(" :tokenkey                              ,   ");
			sb.append(" :expiresin                             ,  ");
			sb.append(" :issuedat                              ,  ");
			sb.append(" :issuer                                ,  ");
			sb.append(" :refreshtoken                          ,  ");
			sb.append(" :tokentype                             ,  ");
			sb.append(" :clientcodeverifier                    ,  ");
			sb.append(" :grantcode                             ,  ");
			sb.append(" :granttype                             ,  ");
			sb.append(" :nonce                                 ,  ");
			sb.append(" :responsetype                          ,  ");
			sb.append(" :client_clientid                       ,  ");
			sb.append(" :subject_id                              ");
			sb.append("     )                                  ");

			try
			{
				System.out.println("execute query : " + sb.toString());
				System.out.println("args : " + Arrays.toString(args));
				nstmt = new NamedParameterStatement(dbConn, sb.toString());
				nstmt.setString("tokenkey", (String) args[0]);
				nstmt.setLong("expiresin", (Long) args[1]);
				nstmt.setLong("issuedat", (Long) args[2]);
				nstmt.setString("issuer", (String) args[3]);
				nstmt.setString("refreshtoken", (String) args[4]);
				nstmt.setString("tokentype", (String) args[5]);
				nstmt.setString("clientcodeverifier", (String) args[6]);
				nstmt.setString("grantcode", (String) args[7]);
				nstmt.setString("granttype", (String) args[8]);
				nstmt.setString("nonce", (String) args[9]);
				nstmt.setString("responsetype", (String) args[10]);
				nstmt.setString("client_clientid", (String) args[11]);
				nstmt.setString("subject_id", (String) args[12]);

			} catch (SQLException e)
			{
				e.printStackTrace();
			}

			break;
		default:
			throw new UnsupportedOperationException("[" + procNm + "]");

		}

		return nstmt;
	}
	// private PreparedStatement createPreparedStatement(Connection dbConn,
	// String
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
	// sb.append(" join usersubject s ") ;
	// sb.append(" on t.SUBJECT_ID = s.id ") ;
	// sb.append(" JOIN client c ") ;
	// sb.append(" ON t.CLIENT_CLIENTID = c.CLIENTID ") ;
	// sb.append(" WHERE t.tokenkey = ? ") ; // 'null'
	// sb.append(" AND c.clientId = ? ") ;
	// sb.append(" AND s.login = ? ; ") ;
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
	// sb.append(" SELECT t.* FROM RefreshToken t ") ;
	// sb.append(" JOIN usersubject s ") ;
	// sb.append(" ON t.SUBJECT_ID = s.ID ") ;
	// sb.append(" JOIN client c ") ;
	// sb.append(" ON t.CLIENT_CLIENTID = c.CLIENTID ") ;
	// sb.append(" WHERE t.refreshtoken = ? ") ;
	// sb.append(" AND s.login = ? ") ;
	// sb.append(" AND c.clientId = ? ; ") ;
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
