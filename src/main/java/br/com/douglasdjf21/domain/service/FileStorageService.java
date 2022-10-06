package br.com.douglasdjf21.domain.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.douglasdjf21.config.FileStorageConfig;
import br.com.douglasdjf21.exception.FileStorageException;
import br.com.douglasdjf21.exception.MyFileNotFoundException;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    
    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();
        
        this.fileStorageLocation = path;
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Não foi possivel criar o arquivo no diretório informado",e);
        }
        
    }
    
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Filename contem um nome de arquivo inválido");
            }
            
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation,StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
            
        } catch (Exception e) {
            throw new FileStorageException("Não foi possivel armazenar o file: " + fileName,e);
        }
    }
    
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            }else {
                throw new MyFileNotFoundException("File não encontrado");
            }
        } catch (Exception e) {
            throw new MyFileNotFoundException("File não encontrado :"+fileName,e);
        }
    }
}
