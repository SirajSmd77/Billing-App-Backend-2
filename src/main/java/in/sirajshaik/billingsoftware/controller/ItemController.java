package in.sirajshaik.billingsoftware.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.sirajshaik.billingsoftware.io.CategoryRequest;
import in.sirajshaik.billingsoftware.io.CategoryResponse;
import in.sirajshaik.billingsoftware.io.ItemRequest;
import in.sirajshaik.billingsoftware.io.ItemResponse;
import in.sirajshaik.billingsoftware.service.impl.CatefgoryService;
import in.sirajshaik.billingsoftware.service.impl.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class ItemController {
    private  final ItemService itemService;

        @PostMapping("/admin/items")
        @ResponseStatus(HttpStatus.CREATED)
        public ItemResponse addItems(@RequestPart("item") String itemString,
                                        @RequestPart("file") MultipartFile multipartFile){
            ObjectMapper objectMapper = new ObjectMapper();
            ItemRequest request = null;
            try{
                request = objectMapper.readValue(itemString,ItemRequest.class);
                return itemService.add(request,multipartFile);

            }catch (JsonProcessingException e){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception Occured While Parsing the Json"+e.getMessage());
            }
        }

        @GetMapping("/items")
        public List<ItemResponse> readItems(){
            return itemService.fetchItems();

        }

        @DeleteMapping("admin/items/{itemId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public  void removeItem(@PathVariable String itemId){
            try {
                itemService.deleteItems(itemId);
            }catch (Exception e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"items not found" +e.getMessage());
            }
        }
    }

