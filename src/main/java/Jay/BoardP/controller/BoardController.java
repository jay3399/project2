package Jay.BoardP.controller;

import Jay.BoardP.controller.form.BoardAddForm;
import Jay.BoardP.controller.dto.BoardCommentDto;
import Jay.BoardP.controller.dto.BoardDetailedDto;
import Jay.BoardP.controller.dto.BoardListDto;
import Jay.BoardP.controller.dto.BoardPostDto;
import Jay.BoardP.controller.dto.BoardSearch;
import Jay.BoardP.controller.dto.CategoryValue;
import Jay.BoardP.controller.dto.CommentDto;
import Jay.BoardP.controller.dto.FileDto;
import Jay.BoardP.controller.dto.Role;
import Jay.BoardP.controller.dto.User;
import Jay.BoardP.controller.form.PenaltyForm;
import Jay.BoardP.controller.form.BoardEditForm;
import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.Member;
import Jay.BoardP.service.BoardService;
import Jay.BoardP.service.CommentService;
import Jay.BoardP.service.PenaltyService;
import Jay.BoardP.service.RedisService;
import Jay.BoardP.service.memberService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


//@RestController
@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final PenaltyService penaltyService;

    private final RedisService redisService;

    private final RedisTemplate redisTemplate;

    private final memberService memberService;

    @GetMapping("/boards")
    public String boardMainPage(Model model,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC)
        Pageable pageable) {

        Page<BoardListDto> pages = boardService.getPages(pageable);

        int nowPage = pages.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, pages.getTotalPages());

        model.addAttribute("pageList", pages);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

//        return pages;
        return "board/boardList";
    }

    @GetMapping("/boards/{categoryCode}/category")
    public String BoardList(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable,
        @PathVariable("categoryCode") String categoryCode, Model model,
        @ModelAttribute("boardSearch") BoardSearch boardSearch) {


        Page<BoardListDto> boardListV2;

        if (categoryCode.equals("ALL")) {
            if (searchExist(boardSearch)) {
                boardListV2 = boardService.getBoardList(boardSearch, pageable);
            } else {
                boardListV2 = boardService.getBoardWithoutSearch(pageable);
            }
        } else {
            boardListV2 = boardService.getBoardListV2(boardSearch, pageable, categoryCode);
        }

        model.addAttribute("pageList", boardListV2);
        model.addAttribute("categoryV", categoryCode);
        model.addAttribute("boardSearch", boardSearch);

        return "board/boardList";
    }


    private static boolean searchExist(BoardSearch boardSearch) {
        return StringUtils.hasText(boardSearch.getTitle()) || StringUtils.hasText(
            boardSearch.getContent()) || StringUtils.hasText(boardSearch.getNickname())
            || StringUtils.hasText(boardSearch.getComplex());
    }


    @GetMapping("/boards/{boardId}")
    public String getBoardV3WithRedisCount(@PathVariable("boardId") Long boardId, Model model,
        @ModelAttribute("commentDto")
        CommentDto commentForm, HttpServletResponse resp, HttpServletRequest req,
        @AuthenticationPrincipal User user) {

        if (deleteStatus(boardId)) {
            return "redirect:/boards/ALL/category";
        }

        String ipAddress = getIpAddress(req);
        String key = "boardCountPerDay";

        if (redisService.isFirstRequest(ipAddress, boardId)) {
            boardService.viewCountWithRedisWithFirst(boardId);   // viewCnt담기
            redisService.writeRequest(ipAddress, boardId);   // 중복방지

            makeUpdateCount(key); //일일 전체 보드 카운트수
        }

        // 첫요청x
        boardService.viewCountWithRedis(boardId);

        List<BoardCommentDto> commentDto = commentService.findByBoardIdV2(boardId);

        //첫요청일때만 생성 ?!

        String viewCnt = String.valueOf(redisTemplate.opsForValue().get("viewCnt::" + boardId));

        BoardDetailedDto boardDetailedDto = boardService.findBoardOnCount(boardId);

        List<FileDto> boardsFiles = boardService.findBoardsFiles(boardId);


        for (FileDto boardsFile : boardsFiles) {
            System.out.println("boardsFile = " + boardsFile.getFileType());
            System.out.println(
                "boardsFile.getOriginalFilename() = " + boardsFile.getOriginalFilename());
            System.out.println("boardsFile = " + boardsFile.getStoredFilename());
        }

//        Long comments = commentService.totalComments(boardId); 쿼리조회를 포뮬러로

        String nickname = user.getNickname();

        model.addAttribute("nickName", nickname);
        model.addAttribute("board", boardDetailedDto);
        model.addAttribute("comments", commentDto);
        model.addAttribute("files", boardsFiles);
        model.addAttribute("viewCnt", viewCnt);

        return "board/boardDeatailed";

//        return boardDetailedDto;
    }


    private Boolean deleteStatus(Long boardId) {
        return boardService.deleteValidated(boardId);
    }

    @GetMapping("/boards/post")
    public String boardForm(@ModelAttribute("boardForm") BoardAddForm boardAddForm) {
        return "board/boardForm";
    }

    @PostMapping("/boards/post")
    public String addBoard(@Validated @ModelAttribute("boardForm") BoardAddForm boardAddForm,
        BindingResult bindingResult, @AuthenticationPrincipal User user,
        RedirectAttributes redirectAttributes, HttpServletRequest req) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("bindingResult :{}", bindingResult);
            return "board/boardForm";
        }

        // 권한 인증 , 오직 어드민만 공지사항작성가능
        // 프론트단에서 막아놔도 , 스크립트상으로 접근가능 -> 컨트롤러에서 한번더 검증
        if (isNotice(boardAddForm, user)) {
            return "redirect:/boards/post";
        }

        String ipAddress = getIpAddress(req);

        String categoryCode = boardAddForm.getCategoryCode();

        BoardPostDto boardPostDto = boardAddForm.createBoardPostDto(user.getNickname(), ipAddress);

        Long boardId = boardService.addBoardV2(user.getId(

        ), boardPostDto);

        String key = "boardPerDay";

        makeUpdateCount(key);

        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/{boardId}";
    }

    private void makeUpdateCount(String key) {
        if (!redisTemplate.hasKey(key)) {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, 0L);
            valueOperations.increment(key);
        } else {
            redisTemplate.opsForValue().increment(key);
        }
    }

    private static boolean isNotice(BoardAddForm boardAddForm, User user) {
        return boardAddForm.getCategoryCode().equals("NOTICE") && user.getRole() == Role.USER;
    }

    @GetMapping("/boards/{boardId}/report")
    private String reportBoardForm(@ModelAttribute("penalty") PenaltyForm penaltyForm) {
        return "board/boardReportForm";
    }


    @ResponseBody
    @PostMapping("/boards/{boardId}/report")
    private Boolean reportBoard(@PathVariable Long boardId, @AuthenticationPrincipal User user,
        @ModelAttribute
        PenaltyForm penaltyForm) {
        return penaltyService.exist(user.getId(), boardId, penaltyForm);
    }


    @PostMapping("/boards/{boardId}/delete")
    public String deleteBoardV2(@PathVariable Long boardId, @AuthenticationPrincipal User user) {

        Member one = memberService.findOne(user.getId());
        Board board = getBoardValidator(boardId, one.getBoardList());

        if (board == null) {
            return "redirect:/boards/ALL/category";
        }

        boardService.deleteBoardV2(board);
        return "redirect:/boards/ALL/category";
    }

    private static Board getBoardValidator(Long boardId, List<Board> boardList) {
        return boardList.stream().filter(board1 -> board1.getId().equals(boardId)).findFirst()
            .orElseGet(null);
    }

    @GetMapping("/boards/{boardId}/edit")
    public String editBoard(@AuthenticationPrincipal User user, @PathVariable Long boardId,
        Model model, RedirectAttributes redirectAttributes) {

        Board board = boardService.findBoard(boardId);
        Member one = memberService.findOne(user.getId());

        List<Board> boardList = one.getBoardList();

        Board board2 = boardList.stream().filter(
            board1 -> board1.getCreateBy().equals(board.getCreateBy())
        ).findAny().get();

        redirectAttributes.addAttribute("boardId", boardId);

        if (board2 == null) {
            return "redirect:/boards/{boardId}";
        }

        BoardDetailedDto board1 = boardService.findBoardOnCount(boardId);
//        List<File> files = board1.getFiles();

        BoardEditForm boardEditForm = BoardEditForm.createBoardEditForm(board1);

        model.addAttribute("board", boardEditForm);
//        model.addAttribute("orFiles", files);

        return "board/boardEditForm";


    }


    @PostMapping("/boards/{boardId}/edit")
    public String editBoard(@Validated @ModelAttribute("board") BoardEditForm boardEditForm,
        BindingResult bindingResult, @PathVariable Long boardId, @AuthenticationPrincipal User user,
        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "board/boardEditForm";
        }

        boardService.editBoard(boardId, boardEditForm);
        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/{boardId}";

    }


    //    @PostMapping("/boards/{boardId}/delete")
    public String deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal User user) {
        boardService.deleteBoard(boardId, user.getId());
        return "redirect:/boards";
    }


    @ModelAttribute("categoryValues")
    private List<CategoryValue> categoryValues() {
        List<CategoryValue> categoryValues = new ArrayList<>();
        categoryValues.add(new CategoryValue("FREE", "잡담"));
        categoryValues.add(new CategoryValue("QNA", "질문"));
        categoryValues.add(new CategoryValue("FUN", "유머"));

        return categoryValues;
    }


    @ModelAttribute("reportValues")
    private List<CategoryValue> reportValues() {
        List<CategoryValue> reportValues = new ArrayList<>();
        reportValues.add(new CategoryValue("F", "욕설"));
        reportValues.add(new CategoryValue("R", "규정위반"));

        return reportValues;
    }

    private static String getIpAddress(HttpServletRequest req) {

        String ip = req.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = req.getHeader("WL-Proxy-Client-IP"); // 웹로직
        }
        if (ip == null) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = req.getRemoteAddr();
        }

        return ip;
    }

    //    @GetMapping("/boards/{boardId}")
//    public String getBoard(@PathVariable("boardId") Long boardId, Model model,
//        @ModelAttribute("commentDto")
//        CommentDto commentForm, HttpServletResponse resp, HttpServletRequest req,
//        @AuthenticationPrincipal User user) {
//
//
//
//        // 삭제된보드에 접근할때 , 리다이렉트.
//        if (deleteStatus(boardId)) {
//            return "redirect:/boards/ALL/category";
//        }
//
//        List<BoardCommentDto> commentDto = commentService.findByBoardIdV2(boardId);
//
//        Cookie oldCookie = null;
//
//        Cookie[] cookies = req.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("visitCount")) {
//                    System.out.println("visitCount 존재");
//                    oldCookie = cookie;
//                }
//            }
//        }
//
//        if (oldCookie != null) {
//            if (!oldCookie.getValue().contains("[" + boardId.toString() + "]")) {
//                boardService.countUp(boardId);
//                oldCookie.setValue(oldCookie.getValue() + "[" + boardId + "]");
//                oldCookie.setPath("/");
//                oldCookie.setMaxAge(60 * 60 * 24);
//                resp.addCookie(oldCookie);
//            }
//        } else {
//            System.out.println("조회수증가");
//            boardService.countUp(boardId);
//            Cookie newCookie = new Cookie("visitCount", "[" + boardId + "]");
//            newCookie.setPath("/");
//            newCookie.setMaxAge(60 * 60 * 24);
//            resp.addCookie(newCookie);
//        }
//
//        BoardDetailedDto boardDetailedDto = boardService.findBoardOnCount(boardId, commentDto);
//
////        Long comments = commentService.totalComments(boardId); 쿼리조회를 포뮬러로
//
//        String nickname = user.getNickname();
//        model.addAttribute("nickName", nickname);
////        model.addAttribute("comments", comments);
//        model.addAttribute("board", boardDetailedDto);
//
//        return "board/boardDeatailed";
//
////        return boardDetailedDto;
//    }

//
//    //    @GetMapping("/boards")
//    public String BoardMainBySearch(@ModelAttribute("boardSearch") BoardSearch boardSearch,
//        Model model,
//        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable
//    ) {
//
//        // 보드서치 그대로 갖고 , 다시 던져준다음에 페이지리스트에 갖고있도록 설정
//
//
//        Page<BoardListDto> pages = boardService.getBoardList(boardSearch, pageable);
//
//
//        int nowPage = pages.getPageable().getPageNumber() + 1;
//        int startPage = Math.max(nowPage - 4, 1);
//        int endPage = Math.min(nowPage + 9, pages.getTotalPages());
//
//        model.addAttribute("pageList", pages);
//        model.addAttribute("nowPage", nowPage);
////        model.addAttribute("startPage", startPage);
////        model.addAttribute("endPage", endPage);
//
//        return "board/boardList";
//
//
//    }
//
//
//    //    @GetMapping("/boards/{categoryCode}/category")
//    public String boardByCondition(Model model, @PathVariable String categoryCode,
//        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC)
//        Pageable pageable) {
//        Page<BoardListDto> pages = boardService.getPagesByCondition(pageable, categoryCode);
////        setTotalComments(pages);
//
//        int nowPage = pages.getPageable().getPageNumber() + 1;
//        int startPage = Math.max(nowPage - 4, 1);
//        int endPage = Math.min(nowPage + 9, pages.getTotalPages());
//
//        model.addAttribute("pageList", pages);
//        model.addAttribute("nowPage", nowPage);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//
////        return pages;
//        return "board/boardList";
//    }
//





}
