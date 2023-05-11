package com.shop.snackshop.controller;



import com.shop.snackshop.dto.PageRequestDTO;
import com.shop.snackshop.dto.QnaDTO;
import com.shop.snackshop.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/qna")
@Log4j2
@RequiredArgsConstructor//자동주입을 위한 어노테이션
public class QnaController {

    private final QnaService service;//@RequiredArgsConstructor -> final선언

    @GetMapping("/")
    public String index() {

        return "redirect:/qna/list";
    }

    @GetMapping("/list")//리스트(자주묻는질문게시판)를 보여준다.
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("list....." + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/register")//get방식으로 글쓰기 페이지
    public void register() {
        log.info("register get.....");
    }

    //글등록 작업은 get방식에서는 화면을 보여주고, post방식에서는 처리 후에 목록 페이지로 이동하도록 한다.
    @PostMapping("/register")//post방식으로 글쓰기 페이지
    public String registerPost(QnaDTO dto, RedirectAttributes redirectAttributes) {

        log.info("dto...." + dto);

        Long gno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg", gno);
        //redirectAttributes를 이용해서 한번만 화면에서 'msg'라는 변수를 사용할 수 있도록 처리.
        //addFlashAttribute는 단한번만 데이터를 전달하는 용도로 사용한다.
        //결론:모달창 사용하기 위함.

        return "redirect:/qna/list";
    }

    @GetMapping({"/read", "/modify"})//get방식으로 게시글 보는 페이지, 수정페이지
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {

        log.info("gno : " + gno);

        QnaDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }

    @PostMapping("/modify")//post방식으로 gno값을 전달하고, 수정후에는
    public String modify(QnaDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        //수정시에는 수정해야하는 내용(제목,내용,글번호)이 전달되어야 한다.
        //수정된 후에는 목록 페이지로 이동하거나 조회 페이지로 이동해야한다.

        log.info("post modify.......");
        log.info("dto : " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());
        //수정하고 난뒤 페이지가 달라지므로 페이징관련 데이터 처리도 같이 진행한다.
        //수정하고 난뒤 조회 페이지로 리다이렉트 처리될 때 검색 조건을 유지하도록 추가(type,keyword).

        return "redirect:/qna/read";
        //책에는 수정된 후에는 목록페이지로 이동하라고 나와있지만,나는 read로 이동하게 만들겠다.
    }


    @PostMapping("/remove")//삭제는 post방식으로 gno값을 전달하고, 삭제 후에는 다시 목록의 첫 페이지로 이동한다.
    public String remove(long gno, RedirectAttributes redirectAttributes) {

        log.info("gno : " + gno);
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/qna/list";
    }


}
