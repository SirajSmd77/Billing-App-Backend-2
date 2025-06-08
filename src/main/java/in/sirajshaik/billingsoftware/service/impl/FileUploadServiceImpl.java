package in.sirajshaik.billingsoftware.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectAclResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService{

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region.key}")
    private String regionCode;

    private final S3Client s3Client;

    public FileUploadServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    @Override
    public String uploadFile(MultipartFile multipartFile) {
        String  fileNameExtension  = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1);
        String key = UUID.randomUUID().toString()+"."+fileNameExtension;
        try{
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(multipartFile.getContentType())
                    .build();
          // RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
            if(response.sdkHttpResponse().isSuccessful()){
                return "https://"+bucketName+".s3."+regionCode+".amazonaws.com/"+key;
            }else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"An Error Occured While Uploading Image" );
            }
        }catch (IOException e){
              e.printStackTrace();
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occured While Uploading the File"+e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String imgUrl) {
        String fileName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }
}
