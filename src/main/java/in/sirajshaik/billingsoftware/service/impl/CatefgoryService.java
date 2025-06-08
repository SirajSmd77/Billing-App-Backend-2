package in.sirajshaik.billingsoftware.service.impl;

import in.sirajshaik.billingsoftware.io.CategoryRequest;
import in.sirajshaik.billingsoftware.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CatefgoryService {

    CategoryResponse add(CategoryRequest categoryRequest, MultipartFile multipartFile) throws IOException;

    List<CategoryResponse> read();

    void remove(String categoryId);

}
