package city.better.reportservice.services;

import city.better.reportservice.dtos.ImageDto;
import city.better.reportservice.mappers.ImageMapper;
import city.better.reportservice.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ReportService reportService;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public List<ImageDto> getReportImages(Long reportId) {
        reportService.isReportExistsOrElseThrow(reportId);

        var images = imageRepository.findByReportId(reportId);

        return imageMapper.toDto(images);
    }
}
