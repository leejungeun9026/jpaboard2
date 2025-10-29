package com.jungeun.jpaboard2.controller;

import com.jungeun.jpaboard2.dto.BoardDTO;
import com.jungeun.jpaboard2.dto.upload.UploadFileDTO;
import com.jungeun.jpaboard2.dto.upload.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/upload")
public class UpDownController {
  @Value("${com.jungeun.jpaboard2.upload.path}")
  private String uploadPath;

  @GetMapping("/uploadForm")
  public void uploadForm(){
  }

  @PostMapping("/uploadPro")
  public void Upload(UploadFileDTO uploadFileDTO, BoardDTO boardDTO, Model model){
    log.info("uploadFileDTO...... : " + uploadFileDTO);
    log.info("boardDTO...... : " + boardDTO);
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
      model.addAttribute("fileList", list);
    }
  }

  @GetMapping("/view/{filename}")
  public ResponseEntity<Resource> viewFileGet(@PathVariable("filename") String filename){
    Resource resource = new FileSystemResource(uploadPath + File.separator + filename);
    String resourceName = resource.getFilename();
    log.info("resourceName...... : " + resourceName);
    HttpHeaders headers = new HttpHeaders();
    try {
      headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().build();
    }
    log.info(ResponseEntity.ok().headers(headers).body(resource));
    return ResponseEntity.ok().headers(headers).body(resource);
  }

  @GetMapping("/remove")
  public String removeFile(@RequestParam("filename") String filename){
    Resource resource = new FileSystemResource(uploadPath + File.separator + filename);
    log.info("remove resource...... : " + resource);
    String resourceName = resource.getFilename();
    Map<String, Boolean> result = new HashMap<>();
    boolean removed = false;
    try {
      String contentType = Files.probeContentType(resource.getFile().toPath());
      removed = resource.getFile().delete();
      
      if(contentType.startsWith("image")){
        // 썸네일이 있다면 s_삭제하고 오리지날 파일도 지움
        String fileRename = filename.replace("s_", "");
        File originalFile = new File(uploadPath + File.separator + fileRename);
        originalFile.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    result.put("result", removed);
    return "redirect:/upload/uploadForm";
  }
}
