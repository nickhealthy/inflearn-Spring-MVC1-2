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
