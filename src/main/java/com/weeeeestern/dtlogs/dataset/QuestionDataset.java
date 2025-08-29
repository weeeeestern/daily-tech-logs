package com.weeeeestern.dtlogs.dataset;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class QuestionDataset {

    private final Map<String, List<String>> data;

    public QuestionDataset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path path = Path.of("questions/backend.json");
        data = mapper.readValue(Files.newInputStream(path), new TypeReference<>() {});
    }

    public List<String> getQuestions(String category) {
        return data.getOrDefault(category, Collections.emptyList());
    }
}
