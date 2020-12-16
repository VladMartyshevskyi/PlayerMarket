package com.betbull.market.service.impl.mail;

import com.betbull.market.infra.aop.Traced;
import com.betbull.market.service.mail.MailConnection;
import com.betbull.market.service.mail.MailMessage;
import com.betbull.market.service.mail.MailService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Lazy implementation of mail service
 */
@Service
@Profile("dev")
@Lazy
public class GoogleMailServiceImpl implements MailService {

    private final static Logger LOG = LoggerFactory.getLogger(GoogleMailServiceImpl.class);

    /**
     * Loading resources from classpath
     */
    @Value("classpath:mail-configuration.txt")
    private Resource configuration;

    @Override
    @Traced
    public void sendMessage(String email, String message) {
        MailConnection connection = createMailConnection();
        connection.open();
        LOG.debug("GOOGLE SMTP: Sending '" + message + "' to " + email);
        connection.send(new MailMessage(email, message));
        connection.close();
    }

    /**
     * Inject prototype-scoped bean (Mail Connection) to singleton
     * @return mail connection
     */
    @Lookup
    protected MailConnection createMailConnection() {
        return null;
    }

    @PostConstruct
    public void init() throws IOException {
        String conf = IOUtils.toString(configuration.getInputStream(), StandardCharsets.UTF_8);
        LOG.debug("Google Mail service constructed with conf: " + conf);
    }
}
