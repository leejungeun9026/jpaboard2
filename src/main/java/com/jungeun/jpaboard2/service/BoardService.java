package com.jungeun.jpaboard2.service;

import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.domain.BoardImage;
import com.jungeun.jpaboard2.dto.*;

import java.util.List;
import java.util.stream.Collectors;

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
    if(boardDTO.getBoardImageDTOs() != null) {
      boardDTO.getBoardImageDTOs().forEach(imgDTO -> {
        board.addImage(imgDTO.getUuid(), imgDTO.getFilename(), imgDTO.isImage());
      });
    }
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
    List<BoardImageDTO> boardImageDTOs = board.getImageSet().stream()
        .sorted()
        .map(img->imgEntityToDTO(img))
        .collect(Collectors.toList());
    boardDTO.setBoardImageDTOs(boardImageDTOs);
    return boardDTO;
  }

  default BoardImageDTO imgEntityToDTO(BoardImage boardImage){
    return BoardImageDTO.builder()
        .uuid(boardImage.getUuid())
        .filename(boardImage.getFilename())
        .image(boardImage.isImage())
        .ord(boardImage.getOrd())
        .build();
  }
}
