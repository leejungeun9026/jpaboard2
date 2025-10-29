package com.jungeun.jpaboard2.repository;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
  @EntityGraph(attributePaths = {"imageSet"})
  @Query("select b from Board b where b.bno = :bno")
  Optional<Board> findByIdWithImages(long bno);
}
