package com.api.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.SendSmsRequest;
import com.api.exception.PlasgateSmsException;
import com.api.service.PlasgateSmsService;

/**
 * Sends SMS notifications through PlasGate. Use this to send OTPs, policy
 * reminders, or other one-off messages to a customer's phone number, e.g.
 * POST http://localhost:8081/api/sms/send
 * { "to": "855912345678", "message": "Your policy has been approved." }
 */
@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final PlasgateSmsService plasgateSmsService;

    public SmsController(PlasgateSmsService plasgateSmsService) {
        this.plasgateSmsService = plasgateSmsService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendSmsRequest request) {
        if (request.getTo() == null || request.getTo().isBlank()
                || request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "'to' and 'message' are required."));
        }

        try {
            String plasgateResponse = plasgateSmsService.sendSms(request.getTo(), request.getMessage());
            return ResponseEntity.ok(Map.of("message", "SMS sent.", "gatewayResponse", plasgateResponse));
        } catch (PlasgateSmsException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("message", e.getMessage()));
        }
    }
}
