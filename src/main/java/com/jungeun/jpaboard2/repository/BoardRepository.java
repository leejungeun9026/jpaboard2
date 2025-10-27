package com.jungeun.jpaboard2.repository;

import com.jungeun.jpaboard2.domain.Board;
import com.jungeun.jpaboard2.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
}
