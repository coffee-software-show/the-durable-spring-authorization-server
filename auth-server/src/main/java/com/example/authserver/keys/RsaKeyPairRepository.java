package com.example.authserver.keys;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;

interface RsaKeyPairRepository {

    List<RsaKeyPair> findKeyPairs();

    void save(RsaKeyPair rsaKeyPair);

    record RsaKeyPair(String id, Instant created, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    }
}
