package hu.ponte.hr.repository;

import hu.ponte.hr.controller.ImageMeta;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<ImageMeta, String> {
}
