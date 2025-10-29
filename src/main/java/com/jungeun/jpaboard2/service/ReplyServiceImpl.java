package com.jungeun.jpaboard2.service;

import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.domain.Member;
import com.jungeun.jpaboard2.domain.Reply;
import com.jungeun.jpaboard2.dto.BoardDTO;
import com.jungeun.jpaboard2.dto.PageRequestDTO;
import com.jungeun.jpaboard2.dto.PageResponseDTO;
import com.jungeun.jpaboard2.dto.ReplyDTO;
import com.jungeun.jpaboard2.repository.BoardRepository;
import com.jungeun.jpaboard2.repository.MemberRepository;
import com.jungeun.jpaboard2.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
  private final ReplyRepository replyRepository;
  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;

  @Override
  public Long insertReply(ReplyDTO replyDTO) {
    Reply reply = dtoToEntity(replyDTO);
    Board board = boardRepository.findById(replyDTO.getBno()).orElse(null);
    Member member = memberRepository.findByUsername(replyDTO.getAuthor());
    reply.setBoard(board);
    reply.setMember(member);
    Long rno = replyRepository.save(reply).getRno();
    return rno;
  }

  @Override
  public ReplyDTO findById(Long rno) {
    return entityToDTO(replyRepository.findById(rno).orElse(null));
  }

  @Override
  public void modifyReply(ReplyDTO replyDTO) {
    Reply reply = replyRepository.findById(replyDTO.getRno()).orElse(null);
    reply.setContent(replyDTO.getContent());
    replyRepository.save(reply);
  }

  @Override
  public void deleteById(Long rno) {
    replyRepository.deleteById(rno);
  }

//  @Override
//  public List<ReplyDTO> findAllByBoard(Long bno) {
//    return List.of();
//  }

  @Override
  public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable();
    Page<Reply> result = replyRepository.findByBoardIdPage(bno, pageable);
    List<ReplyDTO> dtoList = result.getContent().stream().map(reply->entityToDTO(reply)).collect(Collectors.toList());
    return PageResponseDTO.<ReplyDTO>withAll()
        .pageRequestDTO(pageRequestDTO)
        .dtoList(dtoList)
        .total((int)result.getTotalElements())
        .build();
  }
}
