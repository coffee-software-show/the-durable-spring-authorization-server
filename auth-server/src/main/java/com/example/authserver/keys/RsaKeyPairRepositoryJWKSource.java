package com.example.authserver.keys;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class RsaKeyPairRepositoryJWKSource implements
        JWKSource<SecurityContext>, OAuth2TokenCustomizer<JwtEncodingContext> {

    private final RsaKeyPairRepository keyPairRepository;

    RsaKeyPairRepositoryJWKSource(RsaKeyPairRepository keyPairRepository) {
        this.keyPairRepository = keyPairRepository;
    }

    @Override
    public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
        var keyPairs = this.keyPairRepository.findKeyPairs();
        var result = new ArrayList<JWK>(keyPairs.size());
        for (var keyPair : keyPairs) {
            var rsaKey = new RSAKey.Builder(keyPair.publicKey()).privateKey(keyPair.privateKey()).keyID(keyPair.id()).build();
            if (jwkSelector.getMatcher().matches(rsaKey)) {
                result.add(rsaKey);
            }
        }
        return result;
    }

    @Override
    public void customize(JwtEncodingContext context) {
        var keyPairs = this.keyPairRepository.findKeyPairs();
        var kid = keyPairs.get(0).id();
        context.getJwsHeader().keyId(kid);
    }
}
