package com.jungeun.jpaboard2.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jpa2_reply")
public class Reply extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rno;
  private String content;

//  private String author;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="mno")
  private Member member;

//  private Long bno;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bno")
  private Board board;
}

