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

  // 댓글 등록하기
  @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Long> register(@RequestBody ReplyDTO replyDTO) {
    log.info("ReplyDTO register......" + replyDTO);
    Long rno = replyService.insertReply(replyDTO);
    Map<String, Long> map = new HashMap<>();
    map.put("rno", rno);
    return map;
  }

  // bno로 댓글 리스트 가져오기
  @GetMapping("/list/{bno}")
  public PageResponseDTO<ReplyDTO> getReplies(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {
    return replyService.getListOfBoard(bno, pageRequestDTO);
  }

  // rno로 댓글 1개 가져오기
  @GetMapping("/{rno}")
  public ReplyDTO read(@PathVariable("rno") Long rno) {
    log.info(rno);
    ReplyDTO replyDTO = replyService.findById(rno);
    return replyDTO;
  }

  // rno로 댓글 삭제하기
  @DeleteMapping("/{rno}")
  public Map<String, Long> remove(@PathVariable("rno") Long rno){
    log.info("remove.......");
    replyService.deleteById(rno);
    Map<String, Long> map = new HashMap<>();
    map.put("rno", rno);
    return map;
  }

  @PutMapping(value = "/{rno}", consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, Long> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {
    replyDTO.setRno(rno);
    replyService.modifyReply(replyDTO);
    Map<String, Long> map = new HashMap<>();
    map.put("rno", rno);
    return map;
  }
}
