package in.sirajshaik.billingsoftware.service.impl;

import in.sirajshaik.billingsoftware.entity.CategoryEntity;
import in.sirajshaik.billingsoftware.entity.ItemEntity;
import in.sirajshaik.billingsoftware.io.ItemRequest;
import in.sirajshaik.billingsoftware.io.ItemResponse;
import in.sirajshaik.billingsoftware.repository.CategoryRepository;
import in.sirajshaik.billingsoftware.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final FileUploadService fileUploadService;

    private final CategoryRepository categoryRepository;

    private final ItemRepository itemRepository;



    @Override
    public ItemResponse add(ItemRequest request, MultipartFile file) {
        String imgUrl = fileUploadService.uploadFile(file);
        ItemEntity newItem = convertToEntity(request);
        CategoryEntity existingCategory = categoryRepository.findByCategoryId(request.getCategoryId()).orElseThrow(()->
                new RuntimeException("Category not found : "+request.getCategoryId()));
        newItem.setCategory(existingCategory);
        newItem.setImgUrl(imgUrl);
        newItem = itemRepository.save(newItem);
        return convertToResponse(newItem);
    }

    private ItemResponse convertToResponse(ItemEntity newItem) {
        return ItemResponse.builder()
                .itemId(newItem.getItemId())
                .price(newItem.getPrice())
                .description(newItem.getDescription())
                .imgUrl(newItem.getImgUrl())
                .name(newItem.getName())
                .categoryName(newItem.getCategory().getName())
                .categoryId(newItem.getCategory().getCategoryId())
                .createdAt(newItem.getCreatedAt())
                .updatedAt(newItem.getUpdatedAt())
                .build();
    }

    private ItemEntity convertToEntity(ItemRequest request) {

        return ItemEntity.builder()
                .itemId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
    }

    @Override
    public List<ItemResponse> fetchItems() {
        return itemRepository.findAll().stream()
                .map(itemEntity -> convertToResponse(itemEntity))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItems(String itemId) {
       ItemEntity existingItem = itemRepository.findByItemId(itemId).orElseThrow(()-> new RuntimeException("Item not found "+itemId));
    boolean isFileDeleted =   fileUploadService.deleteFile(existingItem.getImgUrl());

    if(isFileDeleted){
        itemRepository.delete(existingItem);
    }else {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"unable to delete image");
       }
    }
}
