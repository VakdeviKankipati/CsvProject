package com.vakya.csvfile.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.vakya.csvfile.models.ImageDetail;
import com.vakya.csvfile.models.ImageRequest;
import com.vakya.csvfile.repository.ImageRequestRepository;
import com.vakya.csvfile.services.ImageProcessingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    private ImageProcessingService imageProcessingService;
    private ImageRequestRepository imageRequestRepository;

    public ImageController(ImageProcessingService imageProcessingService,
                           ImageRequestRepository imageRequestRepository){
        this.imageProcessingService=imageProcessingService;
        this.imageRequestRepository=imageRequestRepository;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<Map<String, String>> uploadCsv(@RequestParam("file") MultipartFile file) throws IOException {
        // Parse CSV and validate the format
        List<ImageDetail> imageDetails = parseCsv(file);

        // Create a new ImageRequest
        ImageRequest imageRequest = new ImageRequest();
        imageRequest.setRequestId(UUID.randomUUID().toString());
        imageRequest.setStatus("Pending");
        imageRequest.setImageDetails(imageDetails);

        // Save the request and start asynchronous processing
        ImageRequest savedRequest = imageRequestRepository.save(imageRequest);
        imageProcessingService.processImagesAsync(savedRequest);

        // Return request ID
        Map<String, String> response = new HashMap<>();
        response.put("requestId", savedRequest.getRequestId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{requestId}")
    public ResponseEntity<Map<String, Object>> getStatus(@PathVariable String requestId) {
        Optional<ImageRequest> optionalRequest = imageRequestRepository.findByRequestId(requestId);

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Request ID not found"));
        }

        ImageRequest imageRequest = optionalRequest.get();
        Map<String, Object> response = new HashMap<>();
        response.put("requestId", imageRequest.getRequestId());
        response.put("status", imageRequest.getStatus());
        response.put("outputCsvUrl", imageRequest.getOutputCsvUrl());

        return ResponseEntity.ok(response);
    }

    private List<ImageDetail> parseCsv(MultipartFile file) throws IOException {
        // Use OpenCSV or similar library to parse the CSV file
        List<ImageDetail> imageDetails = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVReader csvReader = new CSVReader(reader);
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                ImageDetail imageDetail = new ImageDetail();
                imageDetail.setSerialNumber(line[0]);
                imageDetail.setProductName(line[1]);
                imageDetail.setInputImageUrls(line[2]); // Comma-separated input URLs
                imageDetails.add(imageDetail);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return imageDetails;
    }
}
