package com.demo.si.file.process.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

/**
 * @author Karthik Palanivelu
 */
@MessagingGateway(name = "s3Gateway", defaultRequestChannel = "inputFiles")
public interface IS3EventGateway {
    /**
     * Processes S3 Event Request
     *
     * @param message SI Message with File to be processed payload.
     * @return operation result
     */
    @Gateway
    void processFile(Message<String> message);
}
