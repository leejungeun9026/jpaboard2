package com.jungeun.jpaboard2.service;

import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.domain.Member;
import com.jungeun.jpaboard2.dto.*;
import com.jungeun.jpaboard2.repository.BoardRepository;
import com.jungeun.jpaboard2.repository.MemberRepository;
import com.jungeun.jpaboard2.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {
  @Autowired
  private BoardRepository boardRepository;
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private ReplyRepository replyRepository;

  @Override
  public Long insertBoard(BoardDTO boardDTO) {
    Board board = dtoToEntity(boardDTO);
    Member member = memberRepository.findByUsername(boardDTO.getAuthor());
    board.setMember(member);
    Long bno = boardRepository.save(board).getBno();
    return bno;
  }

  @Override
  public List<BoardDTO> findAllBoard(){
    List<Board> boards = boardRepository.findAll();
    List<BoardDTO> dtos = new ArrayList<>();
    for (Board board : boards) {
      dtos.add(entityToDTO(board));
    }
    return dtos;
  }

  @Override
  public BoardDTO findById(Long bno, int mode) {
//    Board board = boardRepository.findById(bno).orElse(null);
    Board board = boardRepository.findByIdWithImages(bno).orElse(null);
    BoardDTO boardDTO = entityToDTO(board);
    boardDTO.setAuthor(board.getMember().getName());
    if(mode == 1) {
      board.updateReadCount();
      boardRepository.save(board);
    }
    return boardDTO;
  }

  @Override
  public Long updateBoard(BoardDTO boardDTO) {
    Board board = boardRepository.findById(boardDTO.getBno()).orElse(null);
    board.change(boardDTO.getTitle(), boardDTO.getContent());

    if(boardDTO.getBoardImageDTOs() != null) {
      board.removeImage();
      for(BoardImageDTO boardImageDTO : boardDTO.getBoardImageDTOs()) {
        board.addImage(boardImageDTO.getUuid(), boardImageDTO.getFilename(), boardImageDTO.isImage());
      }
    }
    Long bno = boardRepository.save(board).getBno();
    return bno;
  }

  @Override
  public int deleteBoard(Long bno) {
    Board board = boardRepository.findByIdWithImages(bno).orElse(null);
    board.removeImage();
    replyRepository.deleteByBoardId(bno);
    boardRepository.deleteById(bno);

    Optional<Board> existsBoard = boardRepository.findById(bno);
    if (existsBoard.isEmpty()) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable("bno");
    Page<Board> result = boardRepository.searchAll(pageRequestDTO.getTypes(), pageRequestDTO.getKeyword(), pageable);
    List<BoardDTO> dtoList = result.getContent().stream().map(board->entityToDTO(board)).collect(Collectors.toList());
    int total = (int)result.getTotalElements();

    PageResponseDTO<BoardDTO> responseDTO = PageResponseDTO.<BoardDTO>withAll().pageRequestDTO(pageRequestDTO).dtoList(dtoList).total(total).build();
    return responseDTO;
  }

  @Override
  public PageResponseDTO<BoardListReplyCountDTO> getListWithReplyCount(PageRequestDTO pageRequestDTO) {
    String[] types = pageRequestDTO.getTypes();
    String keyword = pageRequestDTO.getKeyword();
    Pageable pageable = pageRequestDTO.getPageable("bno");
    Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

    return PageResponseDTO.<BoardListReplyCountDTO>withAll()
        .pageRequestDTO(pageRequestDTO)
        .dtoList(result.getContent())
        .total((int)result.getTotalElements())
        .build();
  }
}
