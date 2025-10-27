package com.jungeun.jpaboard2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {
  private Long bno;
  private String title;
  private String author;
  private int readcount;
  private LocalDateTime regDate;
  private Long replyCount;
}
