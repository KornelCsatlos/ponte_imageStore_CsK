package hu.ponte.hr.services;

import hu.ponte.hr.domain.Messages;
import hu.ponte.hr.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ImageStoreImplTest {

    private ImageStoreImpl underTest;
    private MultipartFile file;

    @BeforeEach
    public void setup() {
        underTest = new ImageStoreImpl(mock(ImageRepository.class), mock(SignService.class));
        file = mock(MultipartFile.class);
    }

    @Test
    void testSaveImageDoesNotSaveWhenFileIsNullOrEmpty() {
        //GIVEN
        when(file.isEmpty()).thenReturn(true);
        //WHEN
        String result1 = underTest.saveImage(null);
        String result2 = underTest.saveImage(file);
        //THEN
        assertEquals(Messages.EMPTY_OR_NON_EXISTING.value(), result1);
        assertEquals(Messages.EMPTY_OR_NON_EXISTING.value(), result2);
    }

    @Test
    void testSaveImageDoesNotSaveWhenSizeIsMoreThan2MB() {
        //GIVEN
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn((long) (3.0 / 0.00000095367432)); //this is the value of 3MB in bytes
        when(file.getContentType()).thenReturn("image/jpg");
        //WHEN
        String result = underTest.saveImage(file);
        //THEN
        assertEquals(Messages.MORE_THAN_2MB.value(), result);
    }

    @Test
    void testSaveImageDoesNotSaveWhenTheFileIsNotAnImage() {
        //GIVEN
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn((long) (2.0 / 0.00000095367432)); //this is the value of 2MB in bytes
        when(file.getContentType()).thenReturn("word");
        //WHEN
        String result = underTest.saveImage(file);
        //THEN
        assertEquals(Messages.WRONG_FILE_TYPE.value(), result);
    }

    @Test
    void testSaveImageWorksCorrectly() throws IOException {
        //GIVEN
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn((long) (2.0 / 0.00000095367432)); //this is the value of 2MB in bytes
        when(file.getContentType()).thenReturn("image/jpg");
        InputStream inputStream = mock(InputStream.class);
        byte[] image = new byte[2];
        when(file.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(image);
        //WHEN
        String result = underTest.saveImage(file);
        //THEN
        assertEquals(Messages.OK.value(), result);
    }
}