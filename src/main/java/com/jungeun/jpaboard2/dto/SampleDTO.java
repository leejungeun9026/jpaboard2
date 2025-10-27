package com.jungeun.jpaboard2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleDTO {
  private Long id;
  private String name;
  private int age;
  private String address;
}
