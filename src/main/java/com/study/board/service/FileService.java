package com.study.board.service;

import com.study.board.dto.FileDto;
import com.study.board.entity.Board;
import com.study.board.entity.File;
import com.study.board.repository.FileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ModelMapper modelMapper;

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
    public List<FileDto> getFiles(Integer id){
        List<File> fileEntityList = this.fileRepository.findByBoardId(id);
        List<FileDto> fileDtoList = fileEntityList.stream().map(files -> modelMapper.map(files, FileDto.class)).collect(Collectors.toList());
        return fileDtoList;
    }
}