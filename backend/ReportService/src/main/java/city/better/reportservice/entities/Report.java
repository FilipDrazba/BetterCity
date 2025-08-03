package city.better.reportservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    private Double longitude;
    private Double latitude;

    @OneToMany(
            mappedBy = "report",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    List<Image> images;
}
