package com.sac_lishchuk.utils;

import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.response.FileContentResponse;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileActionExecutor {
    @SneakyThrows
    public static FileContentResponse read(String fileName) {
        Path filePath = Path.of("files", fileName);
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        return FileContentResponse.builder()
                .fileName(fileName)
                .content(content)
                .build();
    }

    @SneakyThrows
    public static FileContentResponse write(FileContentActionRequest request, String fileName) {
        Path filePath = Path.of("files", fileName);
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, request.getNewContent(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return FileContentResponse.builder()
                .fileName(fileName)
                .content(request.getNewContent())
                .build();
    }

    @SneakyThrows
    public static FileContentResponse execute(String fileName) {
        ProcessBuilder processBuilder = new ProcessBuilder("wsl", "bash", "files/" + fileName);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Output: " + line);
            }
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.out.println("Error: " + errorLine);
            }
        }
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Bash script execution failed with exit code: " + exitCode);
        }
        return FileContentResponse.builder().fileName(fileName).build();
    }
}
