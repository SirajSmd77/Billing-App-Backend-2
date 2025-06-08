package in.sirajshaik.billingsoftware.repository;

import in.sirajshaik.billingsoftware.entity.OrderEntity;
import in.sirajshaik.billingsoftware.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {


}
