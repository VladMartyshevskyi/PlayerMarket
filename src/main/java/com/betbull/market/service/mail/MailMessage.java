package com.betbull.market.service.mail;

import lombok.Data;

@Data
public class MailMessage {
    private final String email;
    private final String message;
}
