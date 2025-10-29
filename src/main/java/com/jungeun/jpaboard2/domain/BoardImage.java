package com.jungeun.jpaboard2.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="jpa2_board_image")
@Getter @Setter
@ToString(exclude = "board")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardImage implements Comparable<BoardImage>{
  @Id
  private String uuid;
  private String filename;
  private int ord;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name= "bno")
  private Board board;

  public void changeBoard(Board board) {
    this.board = board;
  }

  // OneToMany에서 순서에 맞게 정렬하기 위함
  @Override
  public int compareTo(BoardImage other) {
    return this.ord - other.ord;
  }

}
