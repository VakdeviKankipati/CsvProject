package com.vakya.csvfile.repository;

import com.vakya.csvfile.models.ImageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRequestRepository extends JpaRepository<ImageRequest, Long> {
    Optional<ImageRequest> findByRequestId(String requestId);
}
