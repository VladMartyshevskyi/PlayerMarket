package com.betbull.market.service.mail;

public interface MailConnection {

    void open();

    void send(MailMessage mailMessage);

    void close();
}
