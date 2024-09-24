package com.vakya.csvfile.controller.webhooks;

import com.vakya.csvfile.models.ImageRequest;
import com.vakya.csvfile.repository.ImageRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
public class HandleWebhook {

    private ImageRequestRepository imageRequestRepository;

    public HandleWebhook(ImageRequestRepository imageRequestRepository){
        this.imageRequestRepository=imageRequestRepository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, String> payload) {
        // Handle the webhook payload
        String requestId = payload.get("requestId");
        String status = payload.get("status");

        // Update the request status based on the webhook
        Optional<ImageRequest> optionalRequest = imageRequestRepository.findByRequestId(requestId);
        if (optionalRequest.isPresent()) {
            ImageRequest request = optionalRequest.get();
            request.setStatus(status);
            imageRequestRepository.save(request);
        }

        return ResponseEntity.ok().build();
    }
}
