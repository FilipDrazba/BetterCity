package city.better.reportservice.controller;

import city.better.reportservice.dto.ReportDtoRequest;
import city.better.reportservice.model.Report;
import city.better.reportservice.repository.ReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerIntegrationTest {

    @Container
    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testDb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @AfterAll
    static void tearDown(@Autowired HikariDataSource dataSource) {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        reportRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /reports - should return list of reports")
    void getReports_shouldReturnListOfReports() throws Exception {
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    @DisplayName("GET /reports/{id} - should return report by id")
    void getReportById_shouldReturnReport() throws Exception {
        Report report = reportRepository.save(new Report(null, "desc", 1.0, 2.0, null));

        mockMvc.perform(get("/reports/" + report.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("desc")))
                .andExpect(jsonPath("$.longitude", is(1.0)))
                .andExpect(jsonPath("$.latitude", is(2.0)));
    }

    @Test
    @DisplayName("GET /reports/{id} - should return 404 when report not found")
    void getReportById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/reports/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /reports - should create report with image")
    void createReport_shouldSaveReportWithImage() throws Exception {
        ReportDtoRequest request = new ReportDtoRequest("desc", 1.0, 2.0);
        MockMultipartFile json = new MockMultipartFile("request", "", "application/json",
                objectMapper.writeValueAsBytes(request));
        MockMultipartFile image = new MockMultipartFile(
                "images", "file.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/reports")
                        .file(json)
                        .file(image))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("desc")))
                .andExpect(jsonPath("$.longitude", is(1.0)))
                .andExpect(jsonPath("$.latitude", is(2.0)));
    }

    @Test
    @DisplayName("POST /reports - should fail with 400 Bad Request when image is not provided")
    void createReport_shouldSaveReportWithoutImage() throws Exception {
        ReportDtoRequest request = new ReportDtoRequest("no image", 1.0, 2.0);
        MockMultipartFile json = new MockMultipartFile(
                "request", "", "application/json",
                objectMapper.writeValueAsBytes(request));

        mockMvc.perform(multipart("/reports")
                        .file(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /reports - should return 400 when request part is missing")
    void createReport_shouldReturnBadRequestWhenRequestMissing() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "images", "file.jpg", "image/jpeg", "img".getBytes());

        mockMvc.perform(multipart("/reports")
                        .file(image))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /reports/{id} - should update report")
    void patchReport_shouldUpdateReport() throws Exception {
        Report report = reportRepository.save(
                new Report(null, "desc", 1.0, 2.0, null));
        ReportDtoRequest patch = new ReportDtoRequest("new desc", 10.0, 20.0);

        mockMvc.perform(patch("/reports/" + report.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("new desc")))
                .andExpect(jsonPath("$.longitude", is(10.0)))
                .andExpect(jsonPath("$.latitude", is(20.0)));
    }

    @Test
    @DisplayName("PATCH /reports/{id} - should return 404 when report not found")
    void patchReport_shouldReturnNotFound() throws Exception {
        ReportDtoRequest patch = new ReportDtoRequest("new desc", 10.0, 20.0);

        mockMvc.perform(patch("/reports/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /reports/{id} - should delete report")
    void deleteReport_shouldDeleteReport() throws Exception {
        Report report = reportRepository.save(
                new Report(null, "desc", 1.0, 2.0, null));

        mockMvc.perform(delete("/reports/" + report.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /reports/{id} - should return 404 when report not found")
    void deleteReport_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/reports/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /reports/{id}/images - should return empty list when no images")
    void getReportImages_shouldReturnEmptyList() throws Exception {
        Report report = reportRepository.save(
                new Report(null, "no image", 0.0, 0.0, null));

        mockMvc.perform(get("/reports/" + report.getId() + "/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /reports - should return 400 when description is null")
    void createReport_shouldReturnBadRequestWhenDescriptionNull() throws Exception {
        ReportDtoRequest request = new ReportDtoRequest(null, 1.0, 2.0);
        MockMultipartFile json = new MockMultipartFile("request", "", "application/json",
                objectMapper.writeValueAsBytes(request));
        MockMultipartFile image = new MockMultipartFile(
                "images", "file.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/reports")
                        .file(json)
                        .file(image))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /reports - should return 400 when longitude is out of range")
    void createReport_shouldReturnBadRequestWhenLongitudeOutOfRange() throws Exception {
        ReportDtoRequest request = new ReportDtoRequest("desc", 200.0, 2.0); // > 180
        MockMultipartFile json = new MockMultipartFile("request", "", "application/json",
                objectMapper.writeValueAsBytes(request));
        MockMultipartFile image = new MockMultipartFile("images", "file.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/reports")
                        .file(json)
                        .file(image))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /reports - should return 400 when latitude is out of range")
    void createReport_shouldReturnBadRequestWhenLatitudeOutOfRange() throws Exception {
        ReportDtoRequest request = new ReportDtoRequest("desc", 1.0, -100.0); // < -90
        MockMultipartFile json = new MockMultipartFile("request", "", "application/json",
                objectMapper.writeValueAsBytes(request));
        MockMultipartFile image = new MockMultipartFile("images", "file.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/reports")
                        .file(json)
                        .file(image))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /reports/{id} - should return 400 when patch data is invalid")
    void patchReport_shouldReturnBadRequestWhenInvalid() throws Exception {
        Report report = reportRepository.save(new Report(null, "valid", 1.0, 1.0, null));
        ReportDtoRequest patch = new ReportDtoRequest(null, null, 91.0);

        mockMvc.perform(patch("/reports/" + report.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isBadRequest());
    }

}
