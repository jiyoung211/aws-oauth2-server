package ca.sgicanada.security.oauth2;

import java.util.Properties;

import org.apache.cxf.rs.security.jose.common.PrivateKeyPasswordProvider;

public class PrivateKeyPasswordProviderImpl implements PrivateKeyPasswordProvider {

    private String pasword = "password";

    @Override
    public char[] getPassword(Properties props) {
        return pasword.toCharArray();
    }

    public void setPassword(String password) {
        this.pasword = password;
    }

}
