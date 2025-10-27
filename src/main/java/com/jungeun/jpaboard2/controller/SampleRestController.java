package com.jungeun.jpaboard2.controller;

import com.jungeun.jpaboard2.dto.SampleDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestController {
  @GetMapping("/test")
  public String test() {
    return "test";
  }

  @GetMapping("/test2")
  public SampleDTO test2(){
    SampleDTO sampleDTO = SampleDTO.builder()
        .id(1L)
        .name("test")
        .age(20)
        .address("부산시")
        .build();
    return sampleDTO;
  }
}
