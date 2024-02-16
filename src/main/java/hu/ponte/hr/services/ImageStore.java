package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ImageStore {
    public static final double BYTE_TO_MB_MULTIPLIER = 0.00000095367432;

    private final Logger logger = LoggerFactory.getLogger(ImageStore.class);

    @Autowired
    private ImageRepository imageRepository;

    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getContentType() == null) {
            logger.warn("The given file is empty or doesn't exist");
            return "The given file is empty or doesn't exist";
        }
        if (file.getSize() * BYTE_TO_MB_MULTIPLIER > 2) {
            logger.warn("The file is more than 2MB");
            return "The file is more than 2MB";
        }
        if (!file.getContentType().contains("image")) {
            logger.warn("The file is not an image type");
            return "The file is not an image type";
        }
        try {
            imageRepository.save(ImageMeta.builder()
                    .digitalSign("ds") //TODO: change digitalSign to use SignService for init
                    .name(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .size(file.getSize())
                    .image(file.getInputStream().readAllBytes())
                    .build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "OK";
    }

    public List<ImageMeta> getAllImages() {
        ArrayList<ImageMeta> result = new ArrayList<>();
        imageRepository.findAll().forEach(result::add);
        return result;
    }

    public Optional<ImageMeta> findById(String id) {
        return imageRepository.findById(id);
    }
}
