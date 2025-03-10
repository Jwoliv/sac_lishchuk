package com.sac_lishchuk.utils;

import com.sac_lishchuk.shared.exception.UnknownFileException;
import com.sac_lishchuk.shared.request.FileContentActionRequest;
import com.sac_lishchuk.shared.response.FileContentResponse;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static ResponseEntity<InputStreamResource> readImg(FileContentActionRequest request) {
        String fileName = request.getFileName();
        try {
            byte[] imageBytes = FileActionExecutor.readImageFile(fileName);
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            MediaType mediaType = getMediaTypeForFileExtension(fileExtension);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(imageBytes));
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, mediaType.toString()).body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @SneakyThrows
    private static byte[] readImageFile(String fileName) {
        Path filePath = Path.of("files", fileName);
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        return Files.readAllBytes(filePath);
    }

    @SneakyThrows
    public static FileContentResponse write(FileContentActionRequest request) {
        String fileName = request.getFileName();
        Path filePath = Path.of("files", fileName);
        Files.createDirectories(filePath.getParent());

        List<String> existingLines = Files.exists(filePath) ? Files.readAllLines(filePath, StandardCharsets.UTF_8) : new ArrayList<>();

        switch (request.getAction()) {
            case APPEND -> existingLines.add(request.getNewContent());
            case REMOVE -> existingLines.removeIf(line -> line.contains(request.getTargetContent()));
            case UPDATE -> existingLines.replaceAll(line -> line.contains(request.getTargetContent()) ? request.getNewContent() : line);
            case OVERWRITE -> existingLines = Collections.singletonList(request.getNewContent());
            default -> throw new IllegalArgumentException("Unsupported file operation: " + request.getAction());
        }

        Files.write(filePath, existingLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return FileContentResponse.builder()
                .fileName(fileName)
                .content(String.join("\n", existingLines))
                .build();
    }

    @SneakyThrows
    public static FileContentResponse execute(FileContentActionRequest request) {
        String fileName = request.getFileName();
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

    public static String checkExistenceFile(String fileName) {
        Path filePath = Path.of("files/%s".formatted(fileName));
        if (!Files.exists(filePath)) {
            throw new UnknownFileException(fileName);
        }
        return fileName;
    }




    public static MediaType getMediaTypeForFileExtension(String fileExtension) {
        return switch (fileExtension.toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
