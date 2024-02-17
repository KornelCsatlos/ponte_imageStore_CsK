package hu.ponte.hr.services;

import hu.ponte.hr.domain.ImageMeta;
import hu.ponte.hr.domain.Messages;
import hu.ponte.hr.exception.UnableToCreateSignedHashException;
import hu.ponte.hr.exception.VerificationFailedException;
import hu.ponte.hr.repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ImageStoreImpl implements ImageStore {
    public static final double BYTE_TO_MB_MULTIPLIER = 0.00000095367432;

    private final Logger logger = LoggerFactory.getLogger(ImageStoreImpl.class);

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private SignService signService;

    /**
     * @return final result of the image processing
     */
    @Override
    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getContentType() == null) {
            logger.warn("The given file is empty or doesn't exist");
            return Messages.EMPTY_OR_NON_EXISTING.value();
        }
        if (file.getSize() * BYTE_TO_MB_MULTIPLIER > 2) {
            logger.warn("The file is more than 2MB");
            return Messages.MORE_THAN_2MB.value();
        }
        if (!file.getContentType().contains("image")) {
            logger.warn("The file is not an image");
            return Messages.WRONG_FILE_TYPE.value();
        }
        try {
            imageRepository.save(ImageMeta.builder()
                    .digitalSign(signService.sign(file.getOriginalFilename()))
                    .name(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .size(file.getSize())
                    .image(file.getInputStream().readAllBytes())
                    .build());
        } catch (IOException | UnableToCreateSignedHashException | VerificationFailedException e) {
            logger.warn("Reading the picture caused an exception");
            logger.warn(e.getMessage());
            return Messages.EXCEPTION_WHILE_READING_IMAGE.value();
        }
        logger.info(file.getOriginalFilename() + " was saved");
        return Messages.OK.value();
    }

    @Override
    public List<ImageMeta> getAllImages() {
        ArrayList<ImageMeta> result = new ArrayList<>();
        imageRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public Optional<ImageMeta> findById(String id) {
        return imageRepository.findById(id);
    }
}
