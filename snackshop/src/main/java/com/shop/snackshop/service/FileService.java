package com.shop.snackshop.service;





import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath,String originalFileName,byte[] fileData)throws Exception{
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName =uuid.toString() + extension;
        String fileuploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos =  new FileOutputStream(fileuploadFullUrl);
        fos.write(fileData);
        fos.close();
        return  savedFileName;
    }
    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일이 삭제되었습니다.");

        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
