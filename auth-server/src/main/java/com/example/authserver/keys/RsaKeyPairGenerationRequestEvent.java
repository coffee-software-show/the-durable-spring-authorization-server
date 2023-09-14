package com.example.authserver.keys;

import org.springframework.context.ApplicationEvent;

import java.time.Instant;

class RsaKeyPairGenerationRequestEvent extends ApplicationEvent {

    RsaKeyPairGenerationRequestEvent(Instant instant) {
        super(instant);
    }

    @Override
    public Instant getSource() {
        return (Instant) super.getSource();
    }
}
