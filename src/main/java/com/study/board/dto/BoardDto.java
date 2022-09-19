package com.study.board.dto;

import com.study.board.entity.Board;
import com.study.board.entity.File;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    @Id
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private String filename;
    private String filepath;
    private LocalDateTime date;
    private String writer;
    @Column(nullable = false)
    private Integer view_cnt;

    @Builder
    public BoardDto(Integer id, String title, String content, String filename, String filepath, String writer, LocalDateTime date, Integer view_cnt){
        this.id = id;
        this.title = title;
        this.content = content;
        this.filename = filename;
        this.filepath = filepath;
        this.writer = writer;
        this.date = date;
        this.view_cnt = view_cnt;
    }
    @Builder
    public BoardDto(Board board){
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.date = board.getDate();
        this.view_cnt = board.getView_cnt();
    }
}