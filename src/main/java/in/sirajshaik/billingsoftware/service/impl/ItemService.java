package in.sirajshaik.billingsoftware.service.impl;

import in.sirajshaik.billingsoftware.io.ItemRequest;
import in.sirajshaik.billingsoftware.io.ItemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {


    ItemResponse add(ItemRequest request , MultipartFile file);

    List<ItemResponse> fetchItems();

    void deleteItems(String itemId);
}
