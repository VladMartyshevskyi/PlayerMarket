package com.betbull.market.service.impl.mail;


import com.betbull.market.service.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * AmazonMailService is enabled only in prod profile.
 */
@Service
@Profile("prod")
public class AmazonMailServiceImpl implements MailService {

    private final static Logger LOG = LoggerFactory.getLogger(AmazonMailServiceImpl.class);

    @Override
    public void sendMessage(String email, String message) {
        LOG.debug("AMAZON SMTP: Sending '" + message + "' to " + email);
    }

    @PostConstruct
    public void init() {
        LOG.debug("Amazon Mail service constructed");
    }
}
