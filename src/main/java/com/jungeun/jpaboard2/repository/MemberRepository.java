package com.jungeun.jpaboard2.repository;

import com.jungeun.jpaboard2.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Member findByUsername(String username);
}
