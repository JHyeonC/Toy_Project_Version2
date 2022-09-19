package com.study.board.dto;

import com.study.board.entity.File;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {
    @Id
    @Column(nullable = false)
    private Integer fileid;
    private String originfilename;
    private String uploadfilename;
    private String uploadfilepath;

    @Builder
    public FileDto(File file){
        this.fileid = file.getFileid();
        this.originfilename = file.getOriginfilename();
        this.uploadfilename = file.getUploadfilename();
        this.uploadfilepath = file.getUploadfilepath();
    }
}
