package com.betbull.market.service.impl;

import com.betbull.market.service.HealthDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * HealthDashBoard enabled in dev and dashboard profiles
 */
@Component
@Profile("dev & dashboard")
public class HealthDashboardServiceImpl implements HealthDashboardService {

    private final static Logger LOG = LoggerFactory.getLogger(HealthDashboardServiceImpl.class);

    @Override
    public void showHealthDashBoard() {
        LOG.info("Application health check: all services are working");
    }

    /**
     * Starts when whole Spring context is loaded
     */
    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        showHealthDashBoard();
    }

}
