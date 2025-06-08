package in.sirajshaik.billingsoftware.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.sirajshaik.billingsoftware.io.CategoryRequest;
import in.sirajshaik.billingsoftware.io.CategoryResponse;
import in.sirajshaik.billingsoftware.service.impl.CatefgoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CatefgoryService catefgoryService;

//    public CategoryController(CatefgoryService catefgoryService) {
//        this.catefgoryService = catefgoryService;
//    }


    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@RequestPart("category") String categoryString,
                                        @RequestPart("file")MultipartFile multipartFile){
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest request = null;
        try{
               request = objectMapper.readValue(categoryString,CategoryRequest.class);
               return catefgoryService.add(request,multipartFile);

        }catch (JsonProcessingException e){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception Occured While Parsing the Json"+e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/categories")
    public List<CategoryResponse> fetchCategories(){
        return catefgoryService.read();

    }

    @DeleteMapping("admin/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void remove(@PathVariable String categoryId){
        try {
            catefgoryService.remove(categoryId);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
}
