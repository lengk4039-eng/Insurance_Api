package com.api.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.exception.PlasgateSmsException;

/**
 * Sends SMS through the PlasGate REST gateway (https://cloud.plasgate.com).
 * Credentials come from the "Secret"/"Private" key pair shown under
 * SMPP &amp; API &gt; SMS Gateway &gt; API Keys in the PlasGate dashboard.
 *
 * <p>The request body is built by hand rather than via Jackson: the app mixes
 * Jackson 3 ({@code tools.jackson.*}, pulled in by Spring Boot 4's web
 * starter) with a Jackson 2 ObjectMapper that only exists at runtime scope
 * (via the JWT library), so neither is safe to depend on here for this
 * two-field payload.
 */
@Service
public class PlasgateSmsService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${plasgate.base-url}")
    private String baseUrl;

    @Value("${plasgate.private-key}")
    private String privateKey;

    @Value("${plasgate.secret-key}")
    private String secretKey;

    @Value("${plasgate.sender-id}")
    private String senderId;

    /**
     * @param to      recipient phone number in international format, e.g. "855912345678"
     * @param message SMS text
     * @return the raw JSON response body returned by PlasGate
     */
    public String sendSms(String to, String message) {
        if (privateKey == null || privateKey.isBlank() || secretKey == null || secretKey.isBlank()) {
            throw new PlasgateSmsException(
                    "PlasGate credentials are not configured. Set the PLASGATE_PRIVATE_KEY and PLASGATE_SECRET_KEY environment variables.");
        }

        String requestJson = """
                {"globals":{"sender":"%s"},"messages":[{"to":["%s"],"content":"%s"}]}"""
                .formatted(jsonEscape(senderId), jsonEscape(to), jsonEscape(message));

        URI uri = URI.create(baseUrl + "?private_key=" + URLEncoder.encode(privateKey, StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("X-Secret", secretKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new PlasgateSmsException(
                        "PlasGate rejected the SMS request (HTTP " + response.statusCode() + "): " + response.body());
            }
            return response.body();
        } catch (IOException e) {
            throw new PlasgateSmsException("Could not reach the PlasGate SMS gateway", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PlasgateSmsException("SMS request to PlasGate was interrupted", e);
        }
    }

    private static String jsonEscape(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder escaped = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> escaped.append("\\\"");
                case '\\' -> escaped.append("\\\\");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (c < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
                }
            }
        }
        return escaped.toString();
    }
}
