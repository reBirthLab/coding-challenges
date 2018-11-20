package com.mhp.coding.challenges.dependency.notifications;

import com.mhp.coding.challenges.dependency.inquiry.Inquiry;
import com.mhp.coding.challenges.dependency.inquiry.InquiryService;
import com.mhp.coding.challenges.dependency.inquiry.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationHandler implements Subscriber {

    private static final Logger LOG = LoggerFactory.getLogger(PushNotificationHandler.class);

    public PushNotificationHandler(InquiryService inquiryService) {
        inquiryService.addSubscriber(this);
    }

    public void sendNotification(final Inquiry inquiry) {
        LOG.info("Sending push notification for: {}", inquiry);
    }

    @Override
    public void handle(Inquiry inquiry) {
        sendNotification(inquiry);
    }
}
