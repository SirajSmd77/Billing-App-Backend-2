package in.sirajshaik.billingsoftware.io;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;


@Builder
@Data
public class CategoryResponse {

    private String categoryId;

    private String name;

    private String description;

    private String bgColor;

    private String imgUrl;

    private Integer items;


    private Timestamp createdAt;

    private Timestamp updatedAt;
}
