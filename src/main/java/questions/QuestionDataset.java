package questions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility for loading interview questions from the classpath.
 */
public class QuestionDataset {
    private static final String RESOURCE_PATH = "/questions/backend.json";

    /**
     * Reads the question dataset from {@link #RESOURCE_PATH}.
     *
     * @return raw JSON content of the dataset
     * @throws IOException if the resource cannot be read
     */
    public String load() throws IOException {
        try (InputStream is = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                throw new IOException("Resource not found: " + RESOURCE_PATH);
            }
            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }
}
