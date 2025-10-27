package com.jungeun.jpaboard2.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString(exclude = "member")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="jpa2_board")
public class Board extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bno;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 3000)
  private String content;

//  @Column(nullable = false)
//  private String author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mno")
  private Member member;

  @ColumnDefault(value="0")
  private int readcount;

  public void updateReadCount(){
    readcount = readcount + 1;
  }

  public void change(String title, String content){
    this.title = title;
    this.content = content;
  }
}
