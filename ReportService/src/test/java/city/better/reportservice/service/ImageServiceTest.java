package city.better.reportservice.service;

import city.better.reportservice.model.Image;
import city.better.reportservice.dto.ImageDto;
import city.better.reportservice.mapper.ImageMapper;
import city.better.reportservice.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ReportService reportService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private ImageService imageService;

    @Test
    void getReportImages_shouldReturnImageDtos() {
        Long reportId = 1L;
        Image img = Image.builder().id(10L).imageData(new byte[]{1, 2, 3}).build();
        List<Image> images = Collections.singletonList(img);
        ImageDto dto = new ImageDto(10L, "AQID");
        List<ImageDto> dtos = List.of(dto);

        when(imageRepository.findByReportId(reportId)).thenReturn(images);
        when(imageMapper.toDto(images)).thenReturn(dtos);

        List<ImageDto> result = imageService.getReportImages(reportId);

        verify(reportService).isReportExistsOrElseThrow(reportId);
        verify(imageRepository).findByReportId(reportId);
        verify(imageMapper).toDto(images);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }
}
