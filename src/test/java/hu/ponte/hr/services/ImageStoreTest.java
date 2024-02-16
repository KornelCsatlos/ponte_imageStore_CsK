package hu.ponte.hr.services;

import hu.ponte.hr.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ImageStoreTest {

    private ImageStore underTest;
    private MultipartFile file;

    @BeforeEach
    public void setup() {
        underTest = new ImageStore(mock(ImageRepository.class));
        file = mock(MultipartFile.class);
    }

    @Test
    void saveImageDoesNotSaveWhenFileIsNullOrEmpty() {
        //GIVEN
        when(file.isEmpty()).thenReturn(true);
        //WHEN
        String result1 = underTest.saveImage(null);
        String result2 = underTest.saveImage(file);
        //THEN
        assertEquals("The given file is empty or doesn't exist", result1);
        assertEquals("The given file is empty or doesn't exist", result2);
    }

    @Test
    void saveImageDoesNotSaveWhenSizeIsMoreThan2MB() {
        //GIVEN
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn((long) (3.0 / 0.00000095367432)); //this is the value of 3MB in bytes
        when(file.getContentType()).thenReturn("image/jpg");
        //WHEN
        String result = underTest.saveImage(file);
        //THEN
        assertEquals("The file is more than 2MB", result);
    }

    @Test
    void saveImageDoesNotSaveWhenTheFileIsNotAnImage() {
        //GIVEN
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn((long) (2.0 / 0.00000095367432)); //this is the value of 2MB in bytes
        when(file.getContentType()).thenReturn("word");
        //WHEN
        String result = underTest.saveImage(file);
        //THEN
        assertEquals("The file is not an image type", result);
    }

    @Test
    void saveImageWorksCorrectly() {
        //GIVEN
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn((long) (2.0 / 0.00000095367432)); //this is the value of 2MB in bytes
        when(file.getContentType()).thenReturn("image/jpg");
        //WHEN
        String result = underTest.saveImage(file);
        //THEN
        assertEquals("OK", result);
    }
}