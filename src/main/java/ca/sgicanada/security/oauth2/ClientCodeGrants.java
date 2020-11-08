package ca.sgicanada.security.oauth2;

import java.util.LinkedList;
import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;

public class ClientCodeGrants {
    private Client client;
    private List<ServerAuthorizationCodeGrant> codeGrants = new LinkedList<ServerAuthorizationCodeGrant>();
    public ClientCodeGrants(Client c, List<ServerAuthorizationCodeGrant> codeGrants) {
        this.client = c;
        this.codeGrants = codeGrants;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public List<ServerAuthorizationCodeGrant> getCodeGrants() {
        return codeGrants;
    }
    public void setCodeGrants(List<ServerAuthorizationCodeGrant> codeGrants) {
        this.codeGrants = codeGrants;
    }


}
