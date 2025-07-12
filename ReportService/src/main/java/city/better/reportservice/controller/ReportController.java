package city.better.reportservice.controller;

import city.better.reportservice.annotation.RoleRequired;
import city.better.reportservice.dto.*;
import city.better.reportservice.enums.Role;
import city.better.reportservice.service.ImageService;
import city.better.reportservice.service.ReportService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ImageService imageService;

    @GetMapping
    @RoleRequired(Role.EMPLOYEE)
    public ResponseEntity<List<ReportDto>> getAll() {
        var reportsDtos = reportService.getAllReports();

        return ResponseEntity.ok(reportsDtos);
    }

    @GetMapping("/{id}")
    @RoleRequired(Role.USER)
    public ResponseEntity<ReportDto> getById(@PathVariable Long id) {
        var reportDto = reportService.getReportById(id);

        return ResponseEntity.ok(reportDto);
    }

    @Transactional
    @GetMapping("{reportId}/images")
    public ResponseEntity<List<ImageDto>> getReportImages(@PathVariable Long reportId) {
        var images = imageService.getReportImages(reportId);

        return ResponseEntity.ok(images);
    }

    @PostMapping
    @RoleRequired(Role.EMPLOYEE)
    public ResponseEntity<ReportDto> create(@RequestPart("request") @Valid ReportDtoRequest request,
                                            @RequestPart("images") MultipartFile[] files) {
        var savedReportDto = reportService.createReport(request, files);

        return ResponseEntity.ok(savedReportDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReportDto> patch(@PathVariable Long id,
                                           @RequestBody @Valid ReportDtoRequest patch) {
        var updatedReportDto =reportService.updateReportFromPatch(id, patch);

        return ResponseEntity.ok(updatedReportDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
