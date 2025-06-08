package in.sirajshaik.billingsoftware.service.impl;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile multipartFile);

    boolean deleteFile(String imgUrl);
}
