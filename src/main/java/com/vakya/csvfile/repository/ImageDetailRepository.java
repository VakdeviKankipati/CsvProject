package com.vakya.csvfile.repository;

import com.vakya.csvfile.models.ImageDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageDetailRepository extends JpaRepository<ImageDetail, Long> {
}
