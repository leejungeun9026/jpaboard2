package com.jungeun.jpaboard2.controller;

import com.jungeun.jpaboard2.dto.BoardDTO;
import com.jungeun.jpaboard2.dto.BoardListReplyCountDTO;
import com.jungeun.jpaboard2.dto.PageRequestDTO;
import com.jungeun.jpaboard2.dto.PageResponseDTO;
import com.jungeun.jpaboard2.dto.upload.UploadFileDTO;
import com.jungeun.jpaboard2.dto.upload.UploadResultDTO;
import com.jungeun.jpaboard2.service.BoardService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2
@RequestMapping("/board")
public class BoardController {
  @Value("${com.jungeun.jpaboard2.upload.path}")
  private String uploadPath;

  @Autowired
  private BoardService boardService;

//  @GetMapping("/list")
//  public void list(Model model) {
//    log.info("list......");
//    model.addAttribute("boardList", boardService.findAllBoard());
//  }

//  @GetMapping("/list")
//  public void list(PageRequestDTO pageRequestDTO, Model model) {
//    PageResponseDTO<BoardDTO> pageResponseDTO = boardService.getList(pageRequestDTO);
//    log.info(pageResponseDTO);
//    model.addAttribute("pageRequestDTO", pageRequestDTO);
//    model.addAttribute("pageResponseDTO", pageResponseDTO);
//  }

  @GetMapping("/list")
  public void list(PageRequestDTO pageRequestDTO, Model model) {
    PageResponseDTO<BoardListReplyCountDTO> pageResponseDTO = boardService.getListWithReplyCount(pageRequestDTO);
    log.info("pageResponseDTO....." + pageResponseDTO);
    model.addAttribute("pageResponseDTO", pageResponseDTO);
  }

  @GetMapping("/register")
  public void registerGet() {
    log.info("registerGet......");
  }

  @PostMapping("/register")
  public String registerPost(UploadFileDTO uploadFileDTO, @Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    log.info("registerPost......");
    // vallid 처리
    if(bindingResult.hasErrors()) {
      log.info("bindingResult.hasErrors");
      // 1회성으로 error들을 전송
      redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
      return "redirect:/board/register";
    }

    // 첨부파일 넣기
    List<String> strFileNames = null;
    if(uploadFileDTO.getFiles() != null && !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) {
      strFileNames = fileUpload(uploadFileDTO);
    }
    boardDTO.setFilenames(strFileNames);

    // DB insert
    Long bno = boardService.insertBoard(boardDTO);
    if (bno != null) {
      log.info("board inserted successfully....");
      return "redirect:/board/list";
    } else {
      return "redirect:/board/register";
    }
  }

  @GetMapping({"/view", "/modify"})
  public void getBno(@RequestParam("bno") Long bno, @RequestParam("mode") int mode, PageRequestDTO pageRequestDTO, Model model){

    log.info("getboard......");
    model.addAttribute("boardDTO", boardService.findById(bno, mode));
  }

  @PostMapping("/modify")
  public String modify(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
    Long bno = boardService.updateBoard(boardDTO);
    // 전달해야할 값이 많을 경우 RedirectAttributes사용
    redirectAttributes.addAttribute("bno", bno);
    redirectAttributes.addAttribute("mode", 2);
    if (bno != null) {
//      return "redirect:/board/view?mode=2&bno=" + bno;
      return "redirect:/board/view";
    } else {
      return "redirect:/board/modify";
    }
  }

  @GetMapping("/remove")
  public String remove(@RequestParam("bno") Long bno, RedirectAttributes redirectAttributes) {
    int result = boardService.deleteBoard(bno);
    if (result == 1){
      redirectAttributes.addFlashAttribute("result", "삭제 완료");
      return "redirect:/board/list";
    } else {
      return "redirect:/board/view?bno=" + bno;
    }
  }

  private List<String> fileUpload(UploadFileDTO uploadFileDTO) {
    List<String> list = new ArrayList<>();

    // 파일에 값이 있으면 하나씩 저장
    if(uploadFileDTO.getFiles() != null){
      uploadFileDTO.getFiles().forEach(multipartFile -> {
        String originalName = multipartFile.getOriginalFilename();
        log.info("fileNames...... : " + originalName);

        String uuid = UUID.randomUUID().toString();
        Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);

        boolean image = false;
        try {
          multipartFile.transferTo(savePath);

          // 파일이 이미지일 경우
          if(Files.probeContentType(savePath).startsWith("image")){
            image = true;
            File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        list.add(uuid + "_" + originalName);
      });
    }
    return list;
  }
}
