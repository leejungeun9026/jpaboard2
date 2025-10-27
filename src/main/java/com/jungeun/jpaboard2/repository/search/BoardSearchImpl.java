package com.jungeun.jpaboard2.repository.search;

import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.domain.QBoard;
import com.jungeun.jpaboard2.domain.QReply;
import com.jungeun.jpaboard2.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
  public BoardSearchImpl() {
    super(Board.class);
  }

  @Override
  public Page<Board> search1(Pageable pageable) {
    QBoard qboard = QBoard.board;

    JPQLQuery<Board> query = this.from(qboard);
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    booleanBuilder.or(qboard.title.containsIgnoreCase("1"));
    booleanBuilder.or(qboard.content.containsIgnoreCase("1"));

    query.where(new Predicate[]{booleanBuilder});
    query.where(qboard.bno.gt(0L));

    this.getQuerydsl().applyPagination(pageable, query);

    List<Board> list = query.fetch();
    long count = query.fetchCount();

    return new PageImpl<Board>(list, pageable, count);
  }

  @Override
  public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
    QBoard qboard = QBoard.board;
    JPQLQuery<Board> query = this.from(qboard);

    if(types != null && types.length > 0 && keyword != null) {
      BooleanBuilder booleanBuilder = new BooleanBuilder();
      for(String type : types) {
        switch(type) {
          case "t" :
            booleanBuilder.or(qboard.title.containsIgnoreCase(keyword));
            break;
          case "c" :
            booleanBuilder.or(qboard.content.containsIgnoreCase(keyword));
            break;
          case "w" :
            booleanBuilder.or(qboard.member.username.containsIgnoreCase(keyword));
            break;
        }
      }
      query.where(booleanBuilder);
    }
    query.where(new Predicate[]{qboard.bno.gt(0L)});

    this.getQuerydsl().applyPagination(pageable, query);

    List<Board> list = query.fetch();
    long count = query.fetchCount();
    return new PageImpl<Board>(list, pageable, count);
  }

  @Override
  public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
    QBoard qboard = QBoard.board;
    QReply qreply = QReply.reply;

    JPQLQuery<Board> query = this.from(qboard);
    query.leftJoin(qreply).on(qreply.board.eq(qboard));
    query.groupBy(qboard);

    if(types != null && types.length > 0 && keyword != null) {
      BooleanBuilder booleanBuilder = new BooleanBuilder();
      for(String type : types) {
        switch(type) {
          case "t" :
            booleanBuilder.or(qboard.title.containsIgnoreCase(keyword));
            break;
          case "c" :
            booleanBuilder.or(qboard.content.containsIgnoreCase(keyword));
            break;
          case "w" :
            booleanBuilder.or(qboard.member.username.containsIgnoreCase(keyword));
            break;
        }
      }
      query.where(booleanBuilder);
    }
    query.where(qboard.bno.gt(0L));

    JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
        qboard.bno,
        qboard.title,
        qboard.member.username.as("author"),
        qboard.regDate,
        qboard.readcount,
        qreply.count().as("replyCount")
    ));

    this.getQuerydsl().applyPagination(pageable, dtoQuery);

    List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();
    long count = dtoQuery.fetchCount();
    return new PageImpl<>(dtoList, pageable, count);
  }
}
