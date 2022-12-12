package com.epam.esm.security;

import com.epam.esm.domain.Role;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.security.AuthorizationServerConfig.READ_SCOPE;
import static com.epam.esm.security.AuthorizationServerConfig.WRITE_SCOPE;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        modifyUserScopes((SecurityUser) authentication.getPrincipal(), accessToken);
        return accessToken;
    }

    private void modifyUserScopes(SecurityUser user, OAuth2AccessToken accessToken) {
        if (isUser(user) && accessToken.getScope().contains(WRITE_SCOPE)) {
            Set<String> newScopes = accessToken.getScope()
                    .stream()
                    .map(s -> s.equals(WRITE_SCOPE) ? READ_SCOPE : s)
                    .collect(Collectors.toSet());
            ((DefaultOAuth2AccessToken) accessToken).setScope(newScopes);
        }
    }

    private boolean isUser(SecurityUser user) {
        return user.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals(SecurityUser.ROLE_PREFIX + Role.USER));
    }
}
