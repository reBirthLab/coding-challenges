package com.mhp.coding.challenges.dependency.inquiry;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InquiryService {

    private static final Logger LOG = LoggerFactory.getLogger(InquiryService.class);

    private Set<Subscriber> subscribers;

    public InquiryService() {
        subscribers = new HashSet<>();
    }

    public void create(final Inquiry inquiry) {
        LOG.info("User sent inquiry: {}", inquiry);
        notifySubscribers(inquiry);
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribeAll() {
        subscribers.clear();
    }

    private void notifySubscribers(Inquiry inquiry) {
        subscribers.forEach(subscriber -> subscriber.handle(inquiry));
    }
}
