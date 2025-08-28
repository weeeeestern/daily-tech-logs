package slack;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;

/**
 * Service for verifying and interacting with Slack.
 */
public class SlackService {
    private final String signingSecret;
    private final String botToken;

    public SlackService(String signingSecret, String botToken) {
        this.signingSecret = signingSecret;
        this.botToken = botToken;
    }

    /**
     * Verify request from Slack using signing secret.
     *
     * @param timestamp timestamp header from Slack
     * @param signature signature header from Slack
     * @param body      raw request body
     * @return true if request is valid
     */
    public boolean verifySignature(String timestamp, String signature, String body) {
        try {
            String base = "v0:" + timestamp + ":" + body;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(signingSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] digest = mac.doFinal(base.getBytes(StandardCharsets.UTF_8));
            String computed = "v0=" + toHexString(digest);
            return MessageDigest.isEqual(computed.getBytes(StandardCharsets.UTF_8), signature.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return false;
        }
    }

    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * Send a message to Slack using the bot token.
     *
     * @param channel channel id
     * @param text    message text
     * @throws IOException when the Web API call fails
     */
    public void sendMessage(String channel, String text) throws IOException {
        URL url = new URL("https://slack.com/api/chat.postMessage");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + botToken);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoOutput(true);

        String payload = String.format("{\"channel\":\"%s\",\"text\":\"%s\"}", escapeJson(channel), escapeJson(text));
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        if (status >= 200 && status < 300) {
            // consume response stream
            try (InputStream is = conn.getInputStream()) {
                while (is.read() != -1) {
                }
            }
        } else {
            String error = "";
            try (InputStream es = conn.getErrorStream()) {
                if (es != null) {
                    error = readStream(es);
                }
            }
            throw new IOException("Slack API request failed with status " + status + ": " + error);
        }
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String readStream(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}

