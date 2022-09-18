package com.study.board.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity // table
@Getter
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EntityListeners(AuditingEntityListener.class)
@Data
public class Board{ // Board table에 관한 정보
    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    @CreatedDate
    private LocalDateTime date;
    private String writer;
    @Column(name="view_cnt", insertable = false)
    private Integer view_cnt;

    @OneToMany(mappedBy = "board")
    private List<File> fileList = new ArrayList<>();

    public void add(File file){
        file.setBoard(this);
        this.fileList.add(file);
    }

    @Builder
    public Board(Integer id, String title, String content, String writer, Integer view_cnt){
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.view_cnt = view_cnt;
    }
}