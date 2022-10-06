package br.com.douglasdjf21.resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.douglasdjf21.domain.service.FileStorageService;
import br.com.douglasdjf21.dto.UploadFileDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "File", description = "EndPoints para Files")
@RestController
@RequestMapping("/api/v1/file")
public class FilResource {
    
    @Autowired
    private FileStorageService service;
    
    
    @PostMapping(value = "/uploadFile")
    public UploadFileDTO uploadFile(@RequestParam("file") MultipartFile file) {
        var fileName = service.storeFile(file);
        String fileDonwloadUril = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/file/downalodFile/")
                .path(fileName)
                .toUriString();
        return new UploadFileDTO(fileName,fileDonwloadUril,file.getContentType(),file.getSize());
    }
    
    
    @PostMapping(value = "/uploadMultipleFile")
    public List<UploadFileDTO> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) {
      
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
    
    
    @GetMapping(value = "/downalodFile/{filename:.+}")
    public ResponseEntity<Resource> downalodFile(@PathVariable("filename") String filename, HttpServletRequest request) throws IOException {
        Resource resource = service.loadFileAsResource(filename);
        String contentType = "";
        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        if(contentType.isEmpty()) {
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename()+ "\"")
                .body(resource);
    }

}
