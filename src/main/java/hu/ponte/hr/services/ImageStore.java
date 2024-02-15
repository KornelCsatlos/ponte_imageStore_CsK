package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStore {
    public static final double BYTE_TO_MB_MULTIPLIER_IN_BINARY = 0.00000095367432;

    private final Logger logger = LoggerFactory.getLogger(ImageStore.class);

    @Autowired
    ImageRepository imageRepository;

    public String uploadImage(MultipartFile file){
        if(file == null || file.isEmpty()){
            logger.warn("The given file is empty or doesn't exist");
            return "The given file is empty or doesn't exist";
        }
        if(file.getSize() * BYTE_TO_MB_MULTIPLIER_IN_BINARY > 2) {
            logger.warn("The file is more than 2MB");
            return "The file is more than 2MB";
        }
        if(file.getContentType() == null || !file.getContentType().contains("image")){
            logger.warn("The file is not an image type");
            return "The file is not an image type";
        }
        imageRepository.save(ImageMeta.builder()
                .digitalSign(file.getOriginalFilename()) //TODO: change digitalSign to use SignService for init
                .name(file.getName())
                .mimeType(file.getContentType())
                .size(file.getSize())
                .build());
        return "OK";
    }
}
