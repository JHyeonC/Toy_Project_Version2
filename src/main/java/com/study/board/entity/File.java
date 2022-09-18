package com.study.board.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Entity
@Table(name = "file")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileid;
    private String originfilename;
    private String uploadfilename;
    private String uploadfilepath;

    @Builder
    public File(String originfilename, String uploadfilename, String uploadfilepath){
        this.originfilename = originfilename;
        this.uploadfilename = uploadfilename;
        this.uploadfilepath = uploadfilepath;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="board_id")
    private Board board;

}
