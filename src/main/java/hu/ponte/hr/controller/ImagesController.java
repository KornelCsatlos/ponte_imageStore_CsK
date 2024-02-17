package hu.ponte.hr.controller;


import hu.ponte.hr.domain.ImageMeta;
import hu.ponte.hr.services.ImageStore;
import org.h2.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("api/images")
public class ImagesController {
    private final Logger logger = LoggerFactory.getLogger(ImagesController.class);
    @Autowired
    private ImageStore imageStore;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
        return imageStore.getAllImages();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) {
        Optional<ImageMeta> imageMeta = imageStore.findById(id);
        if (imageMeta.isPresent()) {
            InputStream inputStream = new ByteArrayInputStream(imageMeta.get().getImage());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            try {
                IOUtils.copy(inputStream, response.getOutputStream());
                response.setStatus(HttpStatus.OK.value());
                logger.info("Image was loaded");
            } catch (IOException e) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                logger.warn("Unable to load image");
            }
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

}
