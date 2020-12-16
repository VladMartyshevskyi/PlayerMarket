package com.betbull.market.service.impl.mail;

import com.betbull.market.infra.aop.Traced;
import com.betbull.market.service.mail.MailConnection;
import com.betbull.market.service.mail.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Prototype bean implements MailConnection
 */
@Component
@Scope("prototype")
public class GoogleMailConnectionImpl implements MailConnection {

    private final static Logger LOG = LoggerFactory.getLogger(GoogleMailConnectionImpl.class);

    private final Integer connectionId = new Random().nextInt(100);
    private boolean isOpen;

    @Override
    public void open() {
        isOpen = true;
    }

    @Override
    public void send(MailMessage mailMessage) {
        if (isOpen) {
            LOG.debug("Sending " + mailMessage + " via connection: " + connectionId);
        } else {
            LOG.warn("Connection should be opened");
        }

    }

    @Override
    public void close() {
        isOpen = false;
    }

}
