package com.tenco.blog_v1.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

    // DI
    // @Autowired
    private final BoardNativeRepository boardNativeRepository;

//    public BoardController(BoardNativeRepository boardNativeRepository) {
//        this.boardNativeRepository = boardNativeRepository;
//    }

    @GetMapping("/")
    public String index(Model model) {
        List<Board> boardList = boardNativeRepository.findAll();

        model.addAttribute("boardList",boardList);
        log.warn("여기까지 오니");
        return "index";
    }

    // 주소설계 - http://localhost:8080/board/save-form
    // 게시글 작성 화면
    @GetMapping("/board/save-form")
    public String saveForm() {
        return "board/save-form";
    }

    // 주소설계 - http://localhost:8080/board/save
    // 게시글 저장
    @PostMapping("/board/save")
    public String save(@RequestParam(name = "title") String title, @RequestParam(name = "content") String content) {

        // 파라미터가 올바르게 전달 되었는지 확인
        log.warn("세이브 실행: 제목 = {},내용 = {}",title,content);
        boardNativeRepository.save(title,content);

        return "redirect:/";

    }


    // 주소설계 - http://localhost:8080/board/1
    // 특정 게시글 요청 화면
    @GetMapping("/board/{id}")
    public String detail(@PathVariable (name = "id") Integer id, HttpServletRequest request) {
        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board", board);
        return "board/detail";
    }


    // 주소설계 - http://localhost:8080/board/1/delete (form 태그로 활용이기 때문에 delete 선언)
    // form 태그에서는 GET,POST 방식만 지원하기 때문이다.
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable (name = "id") Integer id) {
        boardNativeRepository.deleteById(id);
        return "redirect:/";
    }


    // 게시글 수정 화면 요청
    // board/id/update
    @GetMapping("board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Integer id, HttpServletRequest request) {

        Board board = boardNativeRepository.findById(id);
        request.setAttribute("board", board);
        return "board/update-form"; // src/main/resources/templates/board/update-form.mustache
    }


    // 게시글 수정 요청 기능
    // board/{id}/update
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable(name = "id") Integer id, @RequestParam(name = "title") String title, @RequestParam(name = "content") String content) {
        boardNativeRepository.updateByID(id,title,content);
        return "redirect:/board/" + id;
    }
}
