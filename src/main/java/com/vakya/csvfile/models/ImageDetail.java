package com.vakya.csvfile.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ImageDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serialNumber;
    private String productName;
    private String inputImageUrls; // CSV input, comma-separated URLs
    private String outputImageUrls; // CSV output, comma-separated URLs

    @ManyToOne
    @JoinColumn(name = "image_request_id")
    private ImageRequest imageRequest;
}
