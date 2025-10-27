package com.jungeun.jpademo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="jpa_board")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bno;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 3000)
  private String content;

  @Column(nullable = false)
  private String author;

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
