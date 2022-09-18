package com.study.board.controller;

import com.study.board.dto.BoardDto;
import com.study.board.entity.Board;
import com.study.board.entity.File;
import com.study.board.repository.FileRepository;
import com.study.board.service.BoardService;
import com.study.board.service.FileService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.transaction.Transactional;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/board/write") // localhost:8080/board/write
    public String boardWriteForm(){
        return "boardwrite"; // 어떤 html 파일로 돌려줄건가
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(BoardDto boardDto, Model model,
                                @RequestParam("files") List<MultipartFile> files) throws Exception {
        Board board = boardService.write(boardDto);

        for (MultipartFile multipartFile : files) {
            fileService.saveFile(multipartFile, board);
        }
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/");
        return "message";
    }


    @GetMapping("/")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword){
        Page<BoardDto> list = null;
        if(searchKeyword == null){
            list = this.boardService.boardList(pageable);
        }else{
            list = this.boardService.boardSearchList(searchKeyword, pageable);
        }

        if (list.getTotalElements() == 0){ // 검색결과 없으면 /board/list
            model.addAttribute("message", "검색 결과가 없습니다.");
//            model.addAttribute("searchUrl", "/board/list");
            model.addAttribute("searchUrl", "/");
            return "message";
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());
        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }
    @Transactional
    @GetMapping("/board/view") // localhost:8080/board/view?id = 1
    public String boardView(Model model, Integer id){
        BoardDto boardDto = this.boardService.boardView(id);
        List<File> files = this.fileRepository.findByBoardId(id);
        boardDto.setView_cnt(this.boardService.updateCount(id));
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("all", files);
        return "boardView";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id){
        this.boardService.boardDelete(id);
        return "redirect:/";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model){
        BoardDto boardDto = this.boardService.boardView(id);
        model.addAttribute("boardDto", boardDto);
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, BoardDto boardDto) throws Exception{
        // 파일이 안넘어옴
        this.boardService.write(boardDto);
        return "redirect:/";
    }
    @GetMapping("/attach/{id}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Integer id) throws MalformedURLException {
        File file = this.fileRepository.findById(id).orElse(null);

        UrlResource resource = new UrlResource("file:" + file.getUploadfilepath());
        String encodedFilename = UriUtils.encode(file.getOriginfilename(), StandardCharsets.UTF_8);

        // 파일 다운로드 대화상자가 뜨도록 헤더 설정
        String contentDisposition = "attachment; filename=\"" + encodedFilename + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }
}