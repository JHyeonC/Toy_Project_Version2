package com.study.board.service;

import com.study.board.dto.BoardDto;
import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
public class BoardService {
    @Autowired // 알아서 읽어옴
    private BoardRepository boardRepository;
    @Autowired
    private ModelMapper modelMapper;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }
    @Transactional
    public Board write(BoardDto boardDto) throws Exception{ // 글 작성 처리
        Board entity = modelMapper.map(boardDto, Board.class);
        return boardRepository.save(entity);
    }

    public Page<BoardDto> boardList(Pageable pageable){ // 게시글 리스트 처리
        return this.boardRepository.findAll(pageable).map(BoardDto::new);
    }

    public Page<BoardDto> boardSearchList(String searchKeyword, Pageable pageable){
        return this.boardRepository.findByTitleContaining(searchKeyword, pageable).map(BoardDto::new);
    }
    //특정 게시글 불러오기
    public BoardDto boardView(Integer id){
        Board board = this.boardRepository.findById(id).get();
        BoardDto boardDto = new BoardDto(board);
        return boardDto;
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id){
        this.boardRepository.deleteById(id);
    }

    @Transactional
    public int updateCount(Integer id) {
        return this.boardRepository.updateCount(id);
    }
}