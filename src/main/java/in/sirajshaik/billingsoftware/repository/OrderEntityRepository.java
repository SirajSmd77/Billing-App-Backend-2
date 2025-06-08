package in.sirajshaik.billingsoftware.repository;

import in.sirajshaik.billingsoftware.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

   Optional<OrderEntity> findByOrderId(String orderId);

   List<OrderEntity> findAllByOrderByCreatedAtDesc();




   @Query(value = "SELECT SUM(grand_total) FROM tbl_orders WHERE DATE(created_at) = :date", nativeQuery = true)
   Double sumSalesByDateRange(@Param("date") LocalDate date);

   @Query(value = "SELECT COUNT(*) FROM tbl_orders WHERE DATE(created_at) = :date", nativeQuery = true)
   Long countByOrderDateRange(@Param("date") LocalDate date);


   Page<OrderEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
