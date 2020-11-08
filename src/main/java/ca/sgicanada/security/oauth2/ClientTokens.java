package ca.sgicanada.security.oauth2;

import java.util.LinkedList;
import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;

public class ClientTokens {
    private Client client;
    private List<ServerAccessToken> accessTokens = new LinkedList<ServerAccessToken>();
    private List<RefreshToken> refreshTokens = new LinkedList<RefreshToken>();
    public ClientTokens(Client c,
                              List<ServerAccessToken> accessTokens,
                              List<RefreshToken> refreshTokens) {
        this.client = c;
        this.accessTokens = accessTokens;
        this.refreshTokens = refreshTokens;
    }
    public Client getClient() {
        return client;
    }
    public List<ServerAccessToken> getAccessTokens() {
        return accessTokens;
    }
    public List<RefreshToken> getRefreshTokens() {
        return refreshTokens;
    }
}
