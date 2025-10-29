package com.jungeun.jpaboard2.dto.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO {
  private List<MultipartFile> files;
}
