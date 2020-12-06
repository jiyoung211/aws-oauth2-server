package net.oauth.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

//Read Email <input type="checkbox" name="scopes" value="reademail">, 
//Send Email<input type="checkbox" name="scopes" value="sendemail">,
//Write Board<input type="checkbox" name="scopes" value="writeboard">,
//Read Board<input type="checkbox" name="scopes" value="readboard">,
//access personal info<input type="checkbox" name="scopes" value="personalinfo" checked>,
//calendar<input type="checkbox" name="scopes" value="calendar">,
public class OAuth2Scope {

	// Scope ��� ���
	//OAuth2Scope : �̰��� ������ ���ݿ� �°� �����Ѵ�.
	public static final String SCOPE_PERSONAL_INFO = "personalinfo";
	public static final String SCOPE_READ_EMAIL = "reademail";
	public static final String SCOPE_SEND_EMAIL = "sendemail";
	public static final String SCOPE_WRITE_BOARD = "writeboard";
	public static final String SCOPE_READ_BOARD = "readboard";
	public static final String SCOPE_CALENDAR = "calendar";

	// �� REST ��������Ʈ���� ����� scope ����
	private static HashMap<String, String> scopeUrlMap;
	// Client ��� ȭ�鿡 ������ scope ����
	public static TreeMap<String,String> scopeMsgMap;
	
	static {
		scopeUrlMap = new HashMap<String, String>();
		scopeMsgMap = new TreeMap<String, String>();
		
		// ������ ��Ȳ�� �°� �̰��� �� ��������Ʈ�� scope �� ����Ѵ�.
		// �ش� ��������Ʈ�� ���ؼ��� ������ scope�� �־�߸� ���� �����ϴ�.
		// ��������Ʈ�� ContextPath�� �����ϰ� �Է��Ѵ�.
		scopeUrlMap.put("GET /resource/myinfo.do", SCOPE_PERSONAL_INFO);
		scopeUrlMap.put("GET /resource2/boardlist.do", SCOPE_READ_BOARD);

		//�ʱ�ȭ �̰��� �� ������ scope �� �°� ����Ѵ�.
		//Ŭ���̾�Ʈ �� ��Ͻ� ������ ȭ�鿡�� ����.
		scopeMsgMap.put(SCOPE_READ_EMAIL, "이 메일을 읽을 수 있습니다.");
		scopeMsgMap.put(SCOPE_SEND_EMAIL, "이 메일을 작성하여 전송할 수 있습니다.");
		scopeMsgMap.put(SCOPE_WRITE_BOARD, "게시판에 글을 작성할 수 있습니다.");
		scopeMsgMap.put(SCOPE_READ_BOARD, "게시판의 글을 읽을 수 있습니다.");
		scopeMsgMap.put(SCOPE_PERSONAL_INFO, "사용자 개인 정보를 볼 수 있습니다.");
		scopeMsgMap.put(SCOPE_CALENDAR, "캘린더 기능을 사용할 수 있습니다.");

	}
	
	public static String getScopeFromURI(String uri) {
		return scopeUrlMap.get(uri);
	}

	public static String getScopeMsg(String scopeKey) {
		return scopeMsgMap.get(scopeKey);
	}
	
	public static boolean isScopeExistInMap(String strScope) {
		boolean isValid = true;
		String[] scopes = strScope.split(",");
		for (int i=0; i < scopes.length; i++) {
			String v = getScopeMsg(scopes[i]);
			if (v == null) {
				isValid = false; break;
			}
		}
		
		return isValid;
	}
	
	public static boolean isScopeValid(String receivedScope, String registeredClientScope) {
		String rscopes[] = receivedScope.split(",");
		String temp[] = registeredClientScope.split(",");
		
		List<String> sscopes = Arrays.asList(temp);
		//System.out.println(sscopes);
		boolean isValid = true;
		for (int i=0; i < rscopes.length; i++) {
			if (sscopes.contains(rscopes[i]) == false) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	public static boolean isUriScopeValid(String uriScope, String tokenScopes) {
		String temp[] = tokenScopes.split(",");
		List<String> sscopes = Arrays.asList(temp);
		if (sscopes.contains(uriScope))
			return true;
		else
			return false;
	}
}
