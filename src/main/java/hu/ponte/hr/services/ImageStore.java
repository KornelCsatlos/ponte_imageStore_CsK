package hu.ponte.hr.services;

import hu.ponte.hr.domain.ImageMeta;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ImageStore {
    String saveImage(MultipartFile file);

    List<ImageMeta> getAllImages();

    Optional<ImageMeta> findById(String id);
}
