package hello.springmvc.basic.response;


import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * [요약]
 * @ResponseBody
 * - HTTP BODY에 문자 내용을 직접 반환
 * - viewResolver 대신 HttpMessageConverter(canRead - read, canWrite - write 메서드 사용)가 동작
 *   - 응답의 경우 클라이언트의 Accept 헤더와 서버의 컨트롤러 반환 타입 정보 둘을 조합해서 HttpMessageConverter가 선택됨
 *
 * [HTTP 요청 데이터 읽기 프로세스]
 * 1. HTTP 요청이 오고, 컨트롤러에서 @RequestBody, HttpEntity 파라미터를 사용
 * 2. 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 canRead()를 호출
 * - 대상 클래스 타입을 지원하는지,
 * - HTTP 요청의 Content-type 미디어 타입을 지원하는지
 * 3. canRead() 조건 만족 시 read()를 호출해 객체를 생성해서 반환
 *
 * [HTTP 응답 데이터 생성 프로세스]
 * 1. 컨트롤럴에서 @ResponseBody, HttpEntity로 값을 반환
 * 2. 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 canWrite() 를 호출
 * - 대상 클래스 타입을 지원하는지,
 * - HTTP 요청의 Accept 미디어 타입을 지원하느지(더 정확히는 @RequestMapping의 produces)
 * 3. canWrite() 조건을 만족하면 write()를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성
 */

@Slf4j
@Controller
//@RestController
public class ResponseBodyController {

    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");

    }

    /**
     * HttpEntity, ResponseEntity(Http Status 추가)
     */
    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() {
        // view를 사용하지 않고, ResponseBody를 붙인 것과 같이 HTTP 메시지 컨버터를 통해 HTTP 메시지를 직접 입력할 수 있다.
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }

    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {
        HelloData data = new HelloData();
        data.setUsername("userA");
        data.setAge(20);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    // @ResponseBody를 사용하면 JSON 데이터를 쉽게 보낼 수 있지만, 상태 코드를 보낼 수는 없다.
    // 따라서 @ResponseStatus 애너테이션을 사용해 응답 코드도 보낼 수 있다.
    // 하지만 애너테이션을 사용했기 때문에 동적으로 변경하기는 어려우므로, 동적으로 응답결과를 보내줄 필요가 있을 땐, ResponseEntity를 사용하자.
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        HelloData data = new HelloData();
        data.setUsername("userA");
        data.setAge(20);

        return data;
    }
}
