package city.better.reportservice.services;

import city.better.reportservice.dtos.ReportDto;
import city.better.reportservice.dtos.ReportDtoRequest;
import city.better.reportservice.exceptions.ResourceNotFoundException;
import city.better.reportservice.mappers.ImageMapper;
import city.better.reportservice.mappers.ReportMapper;
import city.better.reportservice.entities.Image;
import city.better.reportservice.entities.Report;
import city.better.reportservice.repositories.ReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private ReportMapper reportMapper;
    @Mock
    ImageMapper imageMapper;

    @InjectMocks
    private ReportService reportService;

    private final Report report1 = new Report(1L, "desc1", 1.0, 1.0, null);
    private final Report report2 = new Report(2L, "desc2", 2.0, 2.0, null);

    private final ReportDto report1Dto = new ReportDto(1L, "desc1", 1.0, 1.0);
    private final ReportDto report2Dto = new ReportDto(2L, "desc2", 2.0, 2.0);

    private final ReportDtoRequest request1 = new ReportDtoRequest("desc1", 1.0, 1.0);

    private final MultipartFile[] files1 = new MockMultipartFile[]{new MockMultipartFile(
            "images", "image.jpg", "image/jpeg", new byte[]{1, 2, 3})};
    private final List<Image> images1 = Collections.singletonList(new Image(3L, new byte[]{1, 2, 3}, report1));


    @Test
    @DisplayName("getReport - when report exists - returns DTO")
    void getReport_reportExists_returnsDto() {
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report1));
        when(reportMapper.toDto(report1)).thenReturn(report1Dto);

        ReportDto result = reportService.getReport(1L);

        assertEquals(report1Dto, result);
    }

    @Test
    @DisplayName("getReport - when report does not exist - throws exception")
    void getReport_reportNotFound_throwsException() {
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reportService.getReport(1L));
    }

    @Test
    @DisplayName("getAllReports - returns list of DTOs")
    void getAllReports_returnsListOfDtos() {
        var reports = List.of(report1, report2);
        when(reportRepository.findAll()).thenReturn(reports);
        when(reportMapper.toDtos(reports)).thenReturn(List.of(report1Dto, report2Dto));

        List<ReportDto> result = reportService.getAllReports();

        assertEquals(2, result.size());
        assertEquals(report1Dto, result.get(0));
        assertEquals(report2Dto, result.get(1));
    }

    @Test
    @DisplayName("createReport - saves and returns DTO")
    void createReport_savesAndReturnsDto() {
        when(reportRepository.save(any(Report.class))).thenReturn(report1);
        when(reportMapper.toEntity(request1)).thenReturn(report1);
        when(imageMapper.toImages(files1, report1)).thenReturn(images1);
        when(reportMapper.toDto(report1)).thenReturn(report1Dto);

        ReportDto result = reportService.createReport(request1, files1);

        verify(reportRepository).save(report1);
        assertEquals(report1Dto, result);

        verifyNoMoreInteractions(reportRepository, reportMapper, imageMapper);
    }

    @Test
    @DisplayName("updateReportFromPatch - updates and returns DTO")
    void updateReport_updatesAndReturnsDto() {
        ReportDtoRequest patch = new ReportDtoRequest("updated", 2.0, 3.0);
        ReportDto updatedDto = new ReportDto(1L, "updated", 2.0, 3.0);

        when(reportRepository.findById(1L)).thenReturn(Optional.of(report1));
        when(reportRepository.save(report1)).thenReturn(report1);
        when(reportMapper.toDto(report1)).thenReturn(updatedDto);

        ReportDto result = reportService.updateReportFromPatch(1L, patch);

        assertEquals(updatedDto, result);
        assertEquals("updated", result.description());
        assertEquals(2.0, result.longitude());
        assertEquals(3.0, result.latitude());
    }

    @Test
    @DisplayName("updateReportFromPatch - when report does not exist - throws exception")
    void updateReport_reportNotFound_throwsException() {
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reportService.updateReportFromPatch(1L, request1));
    }

    @Test
    @DisplayName("delete - deletes existing report")
    void delete_existingReport_deletes() {
        when(reportRepository.existsById(1L)).thenReturn(true);

        reportService.delete(1L);

        verify(reportRepository).existsById(1L);
        verify(reportRepository).deleteById(1L);
    }

    @Test
    @DisplayName("delete - when report does not exist - throws exception")
    void delete_reportNotFound_throwsException() {
        when(reportRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reportService.delete(1L));
    }

    @Test
    @DisplayName("isReportExistsOrElseThrow - when report exists - does not throw exception")
    void isReportExists_reportExists_doesNotThrow() {
        when(reportRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> reportService.isReportExistsOrElseThrow(1L));
    }

    @Test
    @DisplayName("isReportExistsOrElseThrow - when report does not exist - throws exception")
    void isReportExists_reportNotFound_throwsException() {
        when(reportRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> reportService.isReportExistsOrElseThrow(1L));
    }
}
