package in.sirajshaik.billingsoftware.service.impl;

import in.sirajshaik.billingsoftware.entity.CategoryEntity;
import in.sirajshaik.billingsoftware.io.CategoryRequest;
import in.sirajshaik.billingsoftware.io.CategoryResponse;
import in.sirajshaik.billingsoftware.repository.CategoryRepository;
import in.sirajshaik.billingsoftware.repository.ItemRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Builder
public class CategoryServiceImpl implements  CatefgoryService {

    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;

    private final ItemRepository itemRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, FileUploadService fileUploadService,ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.fileUploadService = fileUploadService;
        this.itemRepository = itemRepository;
    }



    @Override
    public CategoryResponse add(CategoryRequest categoryRequest, MultipartFile multipartFile) throws IOException {
      /*    Storing images in AWS S3 bucket  */
       String imageUrl =  fileUploadService.uploadFile(multipartFile);

//        /*  Storing images in local Storage   */

//     String file = UUID.randomUUID().toString()+"."+ StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
//
//
//      Path uploadPath  =  Paths.get("uploads").toAbsolutePath().normalize();
//
//        if (Files.exists(uploadPath) && !Files.isDirectory(uploadPath)) {
//            throw new RuntimeException("A file named 'uploads' already exists and is not a directory.");
//        }
//        Files.createDirectories(uploadPath);
//        Path targetLocation =  uploadPath.resolve(file);
//        Files.copy(multipartFile.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
//       String imageUrl = "http://localhost:8080/uploads/" + file;
        CategoryEntity newCategory = convertToEntity(categoryRequest);
        newCategory.setImgUrl(imageUrl);
        newCategory =   categoryRepository.save(newCategory);
        return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryEntity -> convertToResponse(categoryEntity)).collect(Collectors.toList());
    }

    @Override
    public void remove(String categoryId) {

       CategoryEntity existingCategory =
               categoryRepository.findByCategoryId(categoryId).orElseThrow(() -> new RuntimeException("Category Not Found: "+categoryId));
              fileUploadService.deleteFile(existingCategory.getImgUrl());
               categoryRepository.delete(existingCategory);
    }

    private CategoryResponse convertToResponse(CategoryEntity newCategory) {
         Integer itemCount = itemRepository.countByCategoryId(newCategory.getId());
      return    CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imgUrl(newCategory.getImgUrl())
                 .items(itemCount)
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .build();
    }

    private CategoryEntity convertToEntity(CategoryRequest categoryRequest) {
        return CategoryEntity.builder()
                .categoryId(UUID.randomUUID().toString())  // Generate a unique ID
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .bgColor(categoryRequest.getBgColor())
                .build();
    }

}
