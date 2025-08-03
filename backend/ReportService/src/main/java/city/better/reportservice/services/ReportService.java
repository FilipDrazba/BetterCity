package city.better.reportservice.services;

import city.better.reportservice.dtos.ReportDto;
import city.better.reportservice.dtos.ReportDtoRequest;
import city.better.reportservice.exceptions.ResourceNotFoundException;
import city.better.reportservice.mappers.ImageMapper;
import city.better.reportservice.mappers.ReportMapper;
import city.better.reportservice.entities.Report;
import city.better.reportservice.repositories.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ImageMapper imageMapper;

    @Transactional
    public ReportDto createReport(ReportDtoRequest reportDtoRequest,
                                  MultipartFile[] files) {
        var report = reportMapper.toEntity(reportDtoRequest);
        var images = imageMapper.toImages(files, report);

        report.setImages(images);
        var savedReport = reportRepository.save(report);
        return reportMapper.toDto(savedReport);
    }

    public List<ReportDto> getAllReports() {
        var reports = reportRepository.findAll();

        return reportMapper.toDtos(reports);
    }

    public ReportDto getReport(Long id) {
        var report = getReportById(id);

        return reportMapper.toDto(report);
    }

    public ReportDto updateReportFromPatch(Long id,
                                           ReportDtoRequest patch) {
        var report = getReportById(id);

        reportMapper.updateReportFromPatch(report, patch);
        reportRepository.save(report);

        return reportMapper.toDto(report);
    }

    @Transactional
    public void delete(Long id) {
        if (!reportRepository.existsById(id)) {
            throw notFoundException(id);
        }
        reportRepository.deleteById(id);
    }

    private ResourceNotFoundException notFoundException(Long id) {
        return new ResourceNotFoundException("Report not found with id: " + id);
    }

    public void isReportExistsOrElseThrow(Long id) {
        if (!reportRepository.existsById(id)) {
            throw notFoundException(id);
        }
    }

    private Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> notFoundException(id));
    }

}
