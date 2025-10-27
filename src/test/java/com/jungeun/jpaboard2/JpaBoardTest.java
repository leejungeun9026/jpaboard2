package com.jungeun.jpaboard2;

import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.domain.Member;
import com.jungeun.jpaboard2.domain.Reply;
import com.jungeun.jpaboard2.dto.BoardListReplyCountDTO;
import com.jungeun.jpaboard2.repository.BoardRepository;
import com.jungeun.jpaboard2.repository.MemberRepository;
import com.jungeun.jpaboard2.repository.ReplyRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class JpaBoardTest {
  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private ReplyRepository replyRepository;

  @Test
  public void insertMemberTest(){
    Member member = Member.builder()
                    .name("김길동")
                    .password("1234")
                    .username("kim")
                    .build();
    memberRepository.save(member);
  }

  @Test
  public void insertBoardTest(){
    Member member = memberRepository.findByUsername("kim");
    Board board = Board.builder()
        .title("제목3")
        .content("내용333")
        .member(member)
        .build();
    boardRepository.save(board);
  }

  @Test
  public void insetReplyTest(){
    Member member = memberRepository.findByUsername("kim");
    Board board = boardRepository.findById(2L).orElse(null);
    Reply reply = Reply.builder()
        .member(member)
        .board(board)
        .content("다른 글 댓글")
        .build();
    replyRepository.save(reply);
  }

//  @Test
//  public void getReplyTest(){
//    List<Reply> replies = replyRepository.findByBoardId(1L);
//    for(Reply reply : replies){
//      log.info(reply);
//    }
//  }

  @Test
  public void searchReplyCountTest(){
    String[] types = {"t", "c"};
    String keyword = "내용";
    Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
    Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

    log.info(result.getTotalPages());
    log.info(result.getSize());
    log.info(result.getNumber());
    log.info(result.hasPrevious() + ": " + result.hasNext());
    result.getContent().forEach(board -> log.info(board));
  }
}
