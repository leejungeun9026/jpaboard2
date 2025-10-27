package com.jungeun.jpaboard2;

import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.domain.Member;
import com.jungeun.jpaboard2.repository.BoardRepository;
import com.jungeun.jpaboard2.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class JpaBoardTest {
  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private BoardRepository boardRepository;

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
    Board board = Board.builder()
        .title("제목입니다")
        .content("내용입니다")
        .member(memberRepository.findByUsername("lee"))
        .build();
    boardRepository.save(board);
  }
}
