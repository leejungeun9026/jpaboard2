package com.jungeun.jpaboard2.controller;

import com.jungeun.jpaboard2.dto.PageRequestDTO;
import com.jungeun.jpaboard2.dto.PageResponseDTO;
import com.jungeun.jpaboard2.dto.ReplyDTO;
import com.jungeun.jpaboard2.service.ReplyService;
import jakarta.persistence.PreUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/replies")
public class ReplyController {
  @Autowired
  private ReplyService replyService;

  @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Long> register(@RequestBody ReplyDTO replyDTO) {
    log.info(replyDTO.toString());
    Map<String, Long> map = new HashMap<>();
    Long rno = replyService.insertReply(replyDTO);
    map.put("rno", rno);
    return map;
  }

  @GetMapping("/list/{bno}")
  public PageResponseDTO<ReplyDTO> getReplies(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {
    return replyService.getListOfBoard(bno, pageRequestDTO);
  }
}
