package city.better.reportservice.service;

import city.better.reportservice.dto.ImageDto;
import city.better.reportservice.mapper.ImageMapper;
import city.better.reportservice.repository.ImageRepository;
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
