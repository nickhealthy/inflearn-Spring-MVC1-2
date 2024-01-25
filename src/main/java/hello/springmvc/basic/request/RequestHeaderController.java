package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Controller의 사용 가능한 파라미터 목록
 * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann- arguments
 * @Contorller의 사용 가능한 응답 값 목록
 * https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/return-types.html
 */

@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/header")
    public String headers(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMethod httpMethod, // HTTP 메서드를 조회
            Locale locale, // Locale 정보를 조회
            // 모든 HTTP header를 MultiValueMap 형식으로 조회
            // MAP과 유사함, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용
            @RequestHeader MultiValueMap<String, String> headerMap,
            // 특정 HTTP 헤더를 조회한다.
            @RequestHeader("host") String host,
            // 특정 쿠키를 조회한다.
            @CookieValue(value = "myCookie", required = false) String cookie
    ) {

        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);
        return "ok";

    }


}
