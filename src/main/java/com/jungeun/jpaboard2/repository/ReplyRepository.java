package com.jungeun.jpaboard2.repository;

import com.jungeun.jpaboard2.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
//  @Query("select r from Reply r where r.board.bno = :bno")
//  List<Reply> findByBoardId(Long bno);

  @Query("select r from Reply r where r.board.bno = :bno")
  Page<Reply> findByBoardIdPage(Long bno, Pageable pageable);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from Reply r where r.board.bno = :bno")
  void deleteByBoardId(Long bno);
}
