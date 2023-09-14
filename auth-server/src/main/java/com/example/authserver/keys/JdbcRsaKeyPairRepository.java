package com.example.authserver.keys;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
class JdbcRsaKeyPairRepository implements RsaKeyPairRepository {

    private final JdbcTemplate jdbc;

    private final RsaPublicKeyConverter rsaPublicKeyConverter;

    private final RsaPrivateKeyConverter rsaPrivateKeyConverter;

    private final RowMapper<RsaKeyPair> keyPairRowMapper;

    JdbcRsaKeyPairRepository(
            RowMapper<RsaKeyPair> keyPairRowMapper,
            RsaPublicKeyConverter publicKeySerializer,
            RsaPrivateKeyConverter privateKeySerializer,
            JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.keyPairRowMapper = keyPairRowMapper;
        this.rsaPublicKeyConverter = publicKeySerializer;
        this.rsaPrivateKeyConverter = privateKeySerializer;
    }

    @Override
    public List<RsaKeyPair> findKeyPairs() {
        return this.jdbc.query("select * from rsa_key_pairs order by created desc",
                this.keyPairRowMapper);
    }

    @Override
    public void save(RsaKeyPair keyPair) {
        var sql = """
                insert into rsa_key_pairs (id, private_key, public_key, created) values (?, ?, ?, ?)
                on conflict on constraint rsa_key_pairs_id_created_key do nothing
                """;
        try (var privateBaos = new ByteArrayOutputStream(); var publicBaos = new ByteArrayOutputStream()) {
            this.rsaPrivateKeyConverter.serialize(keyPair.privateKey(), privateBaos);
            this.rsaPublicKeyConverter.serialize(keyPair.publicKey(), publicBaos);
            var updated = this.jdbc.update(sql,
                    keyPair.id(),
                    privateBaos.toString(),
                    publicBaos.toString(),
                    new Date(keyPair.created().toEpochMilli()));
            Assert.state(updated == 0 || updated == 1, "no more than one record should have been updated");
        }//
        catch (IOException e) {
            throw new IllegalArgumentException("there's been an exception", e);
        }
    }
}
