package com.api.dto;

/** Request body for POST /api/sms/send. */
public class SendSmsRequest {

    /** Recipient phone number in international format, e.g. "855912345678". */
    private String to;
    private String message;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
