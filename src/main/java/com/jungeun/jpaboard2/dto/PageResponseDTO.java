package com.jungeun.jpaboard2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
  private int page;   // 현재 페이지 번호
  private int size;   // 한 페이지당 표시할 리스트 개수
  private int total;  // 전체 데이터 개수
  private int pageBlockSize; // 한 번에 표시할 페이지 번호 개수

  private int start;  // 페이지 블록 시작
  private int end;    // 페이지 블록 마지막
  private boolean prev; // 이전 블록 유무
  private boolean next; // 다음 블록 유무
  private List<E> dtoList;  // 실제 데이터 목록

  @Builder(builderMethodName = "withAll")
  public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {
    this.page = pageRequestDTO.getPage();
    this.size = pageRequestDTO.getSize();
    this.total = total;
    this.pageBlockSize = 3;
    this.dtoList = dtoList;

    // 전체 마지막 페이지 (total이 0이면 1페이지로 간주)
    int lastPage = Math.max(1, (int) Math.ceil((double) total / this.size));

    this.end = (int) (Math.ceil(this.page / (double) this.pageBlockSize)) * this.pageBlockSize;
    this.start = this.end - (this.pageBlockSize - 1);
    // 보정
    if (this.end > lastPage) this.end = lastPage;
    if (this.start < 1) this.start = 1;

    // 이전/다음 블록 여부
    this.prev = this.start > 1;
    this.next = this.end < lastPage;
  }
}
