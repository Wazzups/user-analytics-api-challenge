package com.wazzups.analytics.userapi.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wazzups.analytics.userapi.model.User;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonLoader {

    public static List<User> loadUsersFromJsonFile(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(new File(path), new TypeReference<List<User>>() {});
    }
}
