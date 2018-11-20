package com.mhp.coding.challenges.dependency.notifications;

import com.mhp.coding.challenges.dependency.inquiry.Inquiry;
import com.mhp.coding.challenges.dependency.inquiry.InquiryService;
import com.mhp.coding.challenges.dependency.inquiry.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailHandler implements Subscriber {

    private static final Logger LOG = LoggerFactory.getLogger(EmailHandler.class);

    public EmailHandler(InquiryService inquiryService) {
        inquiryService.addSubscriber(this);
    }

    public void sendEmail(final Inquiry inquiry) {
        LOG.info("Sending email for: {}", inquiry);
    }

    @Override
    public void handle(Inquiry inquiry) {
        sendEmail(inquiry);
    }
}
