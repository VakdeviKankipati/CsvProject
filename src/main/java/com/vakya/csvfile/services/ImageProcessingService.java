package com.vakya.csvfile.services;

import com.vakya.csvfile.models.ImageDetail;
import com.vakya.csvfile.models.ImageRequest;
import com.vakya.csvfile.repository.ImageDetailRepository;
import com.vakya.csvfile.repository.ImageRequestRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageProcessingService {
    private ImageDetailRepository imageDetailRepository;
    private ImageRequestRepository imageRequestRepository;

    public ImageProcessingService(ImageRequestRepository imageRequestRepository, ImageDetailRepository imageDetailRepository) {
        this.imageDetailRepository = imageDetailRepository;
        this.imageRequestRepository = imageRequestRepository;
    }

    @Async
    public void processImagesAsync(ImageRequest imageRequest) {
        imageRequest.setStatus("Processing");
        imageRequestRepository.save(imageRequest);

        // Iterate over all image details for this request
        for (ImageDetail imageDetail : imageRequest.getImageDetails()) {
            // Download, process, and store images
            List<String> outputUrls = new ArrayList<>();
            String[] inputUrls = imageDetail.getInputImageUrls().split(",");
            for (String inputUrl : inputUrls) {
                // Image compression logic (50% quality reduction)
                String outputUrl = compressAndSaveImage(inputUrl);
                outputUrls.add(outputUrl);
            }

            // Update the ImageDetail entity with output image URLs
            imageDetail.setOutputImageUrls(String.join(",", outputUrls));
            imageDetailRepository.save(imageDetail);
        }

        // Mark the request as completed and generate the output CSV
        imageRequest.setStatus("Completed");
        String outputCsvUrl = generateOutputCsv(imageRequest);
        imageRequest.setOutputCsvUrl(outputCsvUrl);

        imageRequestRepository.save(imageRequest);

        // Trigger Webhook (Optional)
        triggerWebhook(imageRequest);
    }

    private String compressAndSaveImage(String imageUrl) {
        // Simulate image download and compression (You can use ImageMagick, Pillow, etc.)
        return imageUrl.replace(".jpg", "-output.jpg");  // Example of processed image URL
    }

    private String generateOutputCsv(ImageRequest request) {
        // Generate and store the output CSV
        // Return a URL to the CSV (could be stored on AWS S3 or locally)
        return "https://example.com/output/" + request.getRequestId() + ".csv";
    }

    private void triggerWebhook(ImageRequest imageRequest) {
        // Optional: Implement webhook call
    }

}
