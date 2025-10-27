package com.jungeun.jpaboard2.service;

import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.dto.BoardDTO;
import com.jungeun.jpaboard2.dto.BoardListReplyCountDTO;
import com.jungeun.jpaboard2.dto.PageRequestDTO;
import com.jungeun.jpaboard2.dto.PageResponseDTO;

import java.util.List;

public interface BoardService {
  Long insertBoard(BoardDTO boardDTO);
  List<BoardDTO> findAllBoard();
  BoardDTO findById(Long bno, int mode);
  Long updateBoard(BoardDTO boardDTO);
  int deleteBoard(Long bno);
  PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);
  PageResponseDTO<BoardListReplyCountDTO> getListWithReplyCount(PageRequestDTO pageRequestDTO);

  default Board dtoToEntity(BoardDTO boardDTO){
    Board board = Board.builder()
        .bno(boardDTO.getBno())
        .title(boardDTO.getTitle())
        .content(boardDTO.getContent())
        .build();
    return board;
  }

  default BoardDTO entityToDTO(Board board){
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(board.getBno())
        .title(board.getTitle())
        .content(board.getContent())
        .readcount(board.getReadcount())
        .regDate(board.getRegDate())
        .updateDate(board.getUpdateDate())
        .build();
    return boardDTO;
  }
}
