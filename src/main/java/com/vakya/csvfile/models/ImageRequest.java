package com.vakya.csvfile.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class ImageRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;
    private String status; // Pending, Processing, Completed, Failed
    private String outputCsvUrl;

    @OneToMany(mappedBy = "imageRequest", cascade = CascadeType.ALL)
    private List<ImageDetail> imageDetails;
}
