package com.shoescms.domain.etc;


import com.shoescms.common.model.FileEntity;
import com.shoescms.common.service.FileUploadService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "15. Etc")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/etc")
public class CommonController {
    private final FileUploadService fileUploadService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileEntity uploadFile(@RequestParam(required = false) String path,
                                 @Parameter(required = true, name = "file", description = "파일 리스트")
                                 @RequestPart MultipartFile file){
        try{
            return fileUploadService.uploadFile(path, file);
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("message");
        }
    }
}
