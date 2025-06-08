package in.sirajshaik.billingsoftware.entity;

import in.sirajshaik.billingsoftware.io.PaymentDetails;
import in.sirajshaik.billingsoftware.io.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_orders")
public class OrderEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique = true)
        private String orderId;
        private String customerName;
        private String phoneNumber;
        private Double subTotal;
        private Double tax;
        private Double grandTotal;
        @CreationTimestamp
        @Column(updatable = false)
        private LocalDateTime createdAt;
        @UpdateTimestamp
        private Timestamp updatedAt;


        @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
        @JoinColumn(name="order_id")
        private List<OrderItemEntity> items = new ArrayList<>();

        @Embedded
        private PaymentDetails paymentDetails;

        @Enumerated(EnumType.STRING)
        private PaymentMethod paymentMethod;



        @PrePersist
        protected void onCreate(){
            this.orderId = "ORD"+ System.currentTimeMillis();
            this.createdAt = LocalDateTime.now();
        }
}
