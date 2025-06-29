package city.better.reportservice.mapper;

import city.better.reportservice.dto.ImageDto;
import city.better.reportservice.model.Image;
import city.better.reportservice.model.Report;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    List<ImageDto> toDto(List<Image> images);

    default ImageDto toDto(Image image) {
        if (image == null) return null;
        String base64 = image.getImageData() == null ? null : Base64.getEncoder().encodeToString(image.getImageData());
        return new ImageDto(image.getId(), base64);
    }

    default List<Image> toImages(MultipartFile[] files, Report report) {
        return Arrays.stream(files)
                .map(this::toImage)
                .peek(img -> img.setReport(report))
                .collect(Collectors.toList());
    }

    private Image toImage(MultipartFile file) {
        try {
            return Image.builder()
                    .imageData(file.getBytes())
                    .build();
        } catch (IOException e) {
            throw new UncheckedIOException("Błąd odczytu pliku: " + file.getOriginalFilename(), e);
        }
    }
}
