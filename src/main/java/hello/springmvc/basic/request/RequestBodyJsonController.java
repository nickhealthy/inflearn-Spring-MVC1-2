package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 들어올 데이터와 컨텐츠 타입
 * {"username":"hello", "age":20}
 * content-type: application/json
 */

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * HttpServletRequest를 사용해 직접 HTTP 메시지 바디에서 데이터를 읽어와 문자로 변환
     * 문자로 된 JSON 데이터를 Jackson 라이브러리인 ObjectMapper를 사용해 자바 객체로 변환한다.
     */
    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        // JSON -> 객체(역직렬화)
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());

        response.getWriter().write("ok");
    }


    /**
     * @RequestBody 를 사용해 body 데이터를 직접 반환
     * ObjectMapper를 사용해 json 데이터를 자바 객체로 변환
     *
     * @RequestBody
     * HttpMessageConverter 사용 -> StringHttpMessageConverter 적용 *
     *
     * @ResponseBody
     * - 모든 메서드에 @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(
            @RequestBody String messageBody
    ) throws IOException {
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());
        return "ok";
    }


    /**
     * ObjectMapper 를 사용해 역직렬화 과정이 불편함
     * @RequestBody + 사용자 정의 객체 타입을 사용
     * - JackSon 라이브러리가 'ObjectMapper(messageBody, 사용자정의객체타입)' 를 자동으로 넣어줌
     *
     * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter
     * (content-type: application/json)
     */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(
            @RequestBody HelloData data
    ) throws IOException {
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());
        return "ok";
    }

    /**
     * HttpEntity, @RequestBody 를 사용하면
     * HTTP 메시지 컨버터가 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다.
     *
     */
    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(
            @RequestBody HttpEntity<HelloData> httpEntity
    ) throws IOException {
        HelloData data = httpEntity.getBody();
        log.info("username = {}, age = {}", data.getUsername(), data.getAge());
        return "ok";
    }

    /**
     * HTTP 메시지 컨버터는 문자 뿐 아니라 JSON도 객체로 변환해준다.
     *
     * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter
     * - (content-type: application/json)
     *
     * @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용
     * - (Accept: application/json) // 클라이언트가 받을 수 있는 형식
     */
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;
    }

    // v5에서 HttpEntity 객체 사용
    @ResponseBody
    @PostMapping("/request-body-json-v6")
    public HttpEntity<HelloData> requestBodyJsonV6(@RequestBody HelloData data) {
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return new HttpEntity<>(data);
    }

    /**
     * [정리]
     *
     * @RequestBody 요청
     * - JSON 요청 -> HTTP 메시지 컨버터 -> 객체
     * @ResponseBody 응답
     * - 객체 -> HTTP 메시지 컨버터 -> JSON 응답
     */

}
