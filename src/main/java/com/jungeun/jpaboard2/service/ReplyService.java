package com.jungeun.jpaboard2.service;

import com.jungeun.jpaboard2.domain.Reply;
import com.jungeun.jpaboard2.dto.PageRequestDTO;
import com.jungeun.jpaboard2.dto.PageResponseDTO;
import com.jungeun.jpaboard2.dto.ReplyDTO;

import java.util.List;

public interface ReplyService {
  Long insertReply(ReplyDTO replyDTO);
  ReplyDTO findById(Long rno);
  void modifyReply(ReplyDTO reply);
  void deleteById(Long rno);
//  List<ReplyDTO> findAllByBoard(Long bno);
  PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

  default Reply dtoToEntity(ReplyDTO replyDTO) {
    Reply reply = Reply.builder()
        .rno(replyDTO.getRno())
        .content(replyDTO.getContent())
        .build();
    return reply;
  }

  default ReplyDTO entityToDTO(Reply reply) {
    ReplyDTO replyDTO = ReplyDTO.builder()
        .rno(reply.getRno())
        .content(reply.getContent())
        .regDate(reply.getRegDate())
        .updateDate(reply.getUpdateDate())
        .author(reply.getMember().getUsername())
        .bno(reply.getBoard().getBno())
        .build();
    return replyDTO;
  }
}
