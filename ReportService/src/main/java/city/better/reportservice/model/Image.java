package city.better.reportservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images",
        indexes = {
                @Index(name = "idx_report_id", columnList = "report_id")
        })
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private byte[] imageData;

    @JoinColumn(name = "report_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Report report;
}
