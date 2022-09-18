package com.study.board.service;

import com.study.board.dto.BoardDto;
import com.study.board.entity.Board;
import com.study.board.entity.File;
import com.study.board.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }
    @Value("${file.dir}")
    private String fileDir;

    public Integer saveFile(MultipartFile files, Board board) throws IOException{
        if(files.isEmpty()){
            return null;
        }
        String originalFilename = files.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));

        String uploadFilename = uuid + ext;
        String uploadFilepath = fileDir + uploadFilename;

        File file = File.builder()
                .originfilename(originalFilename)
                .uploadfilename(uploadFilename)
                .uploadfilepath(uploadFilepath)
                .build();


        files.transferTo(new java.io.File(uploadFilepath));

        board.add(file);

        File uploadFile = this.fileRepository.save(file);

        return uploadFile.getFileid();
    }
}
