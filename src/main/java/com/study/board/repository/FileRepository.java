package com.study.board.repository;

import com.study.board.dto.FileDto;
import com.study.board.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findByBoardId(int board_id);
}
