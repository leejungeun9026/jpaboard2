package com.jungeun.jpaboard2.service;

import com.jungeun.jpaboard2.dto.upload.UploadFileDTO;
import com.jungeun.jpaboard2.dto.upload.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class UploadServiceImpl implements UploadService {
  @Value("${com.jungeun.jpaboard2.upload.path}")
  private String uploadPath;

  public List<UploadResultDTO> storeFiles(UploadFileDTO uploadFileDTO){
    List<UploadResultDTO> list = new ArrayList<>();

    // 파일에 값이 있으면 하나씩 저장
    if(uploadFileDTO.getFiles() != null){
      uploadFileDTO.getFiles().forEach(multipartFile -> {
        String originalName = multipartFile.getOriginalFilename();
        log.info("fileNames...... : " + originalName);

        String uuid = UUID.randomUUID().toString();
        Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);

        boolean image = false;
        try {
          multipartFile.transferTo(savePath);

          // 파일이 이미지일 경우
          if(Files.probeContentType(savePath).startsWith("image")){
            image = true;
            File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        list.add(UploadResultDTO.builder()
            .uuid(uuid)
            .filename(originalName)
            .image(image)
            .build());
      });
    }
    return list;
  }
}
