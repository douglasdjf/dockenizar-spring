package br.com.douglasdjf21.dto;

import java.io.Serializable;

public class UploadFileDTO implements Serializable{
    
    private String fileName;
    private String fileDownaloadUri;
    private String type;
    private long size;
    
       
    public UploadFileDTO() {
        super();
    }

    public UploadFileDTO(String fileName, String fileDownaloadUri, String type, long size) {
        super();
        this.fileName = fileName;
        this.fileDownaloadUri = fileDownaloadUri;
        this.type = type;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownaloadUri() {
        return fileDownaloadUri;
    }

    public void setFileDownaloadUri(String fileDownaloadUri) {
        this.fileDownaloadUri = fileDownaloadUri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    
    
    

}
