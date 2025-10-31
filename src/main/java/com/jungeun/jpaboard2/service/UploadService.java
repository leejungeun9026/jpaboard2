package com.jungeun.jpaboard2.service;

import com.jungeun.jpaboard2.dto.upload.UploadFileDTO;
import com.jungeun.jpaboard2.dto.upload.UploadResultDTO;

import java.util.List;

public interface UploadService {
  List<UploadResultDTO> storeFiles(UploadFileDTO uploadFileDTO);
}
