package Jay.BoardP.controller;


import Jay.BoardP.domain.Member;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RedisTemplate redisTemplate;


    //    @GetMapping("/loginHome")
    public String homeLoginV1(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return "home";
        }

        Member member =(Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if ( member == null ) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        String ipAddress = getIpAddress(request);

//        makeUpdateCount("VisitCountPerDay");

        // 첫요청일시
        if (isFirstRequest(ipAddress)) {
            writeRequest(ipAddress);   // 중복방지
            makeUpdateCount(); //일일 전체 보드 카운트수
        }



//        HttpSession session = request.getSession(false);
//
//        if (session == null) {
//            return "homeV2";
//        }
//
//        Member member =(Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//
//        if ( member == null ) {
//            return "homeV2";
//        }
//        model.addAttribute("member", member);
//        Long visitCount = redisService.getVisitCount();
//        System.out.println("visitCount = " + visitCount);
//
//        model.addAttribute("count", redisService.getVisitCount());
        return "homeV2";
    }



    private void makeUpdateCount() {
        if (!redisTemplate.hasKey("VisitCountPerDay")) {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set("VisitCountPerDay", 0L);
            valueOperations.increment("VisitCountPerDay");
        } else {
            redisTemplate.opsForValue().increment("VisitCountPerDay");
        }
    }




    //    @GetMapping("/")
    public String homeLoginV2(
        @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
        Model model) {
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "home";
    }
    public boolean isFirstRequest(String clientAddress) {
        String key = getKey(clientAddress);
        return !redisTemplate.hasKey(key);
    }
    public void writeRequest(String clientAddress) {
        String key = getKey(clientAddress);

        redisTemplate.opsForValue().set(key, clientAddress); // 데이터집계 순간에 모두 딜리트 .
        redisTemplate.expire(key, 1L, TimeUnit.DAYS);
//        redisTemplate.expire(key, 1L, TimeUnit.DAYS);
        //



    }

    private static String getKey(String clientAddress) {

        return "firstVisitReq::" + clientAddress;
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





}
