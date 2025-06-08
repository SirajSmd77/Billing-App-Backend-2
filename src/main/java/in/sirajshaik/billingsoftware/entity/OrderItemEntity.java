package in.sirajshaik.billingsoftware.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_order_items")
public class OrderItemEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String itemId;
        private String name;
        private Double price;
        private Integer quantity;

        @CreationTimestamp
        @Column(updatable = false)
        private LocalDateTime createdAt;
        @UpdateTimestamp
        private Timestamp updatedAt;

}
