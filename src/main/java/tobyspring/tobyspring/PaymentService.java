package tobyspring.tobyspring;

import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class PaymentService {
    //얘는 사용자 입력
    public Payment prepare(Long orderId, String currency, BigDecimal foreignCurrencyAmount) throws IOException {
        //1. 환율 가져오기
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        /*connection.getInputStream()을 통해 서버와의 연결 통로에서 바이트 단위의 원시 데이터를 가져오는 입력 통로를 가져오고
        * new InputStreamReader에서 컴퓨터가 읽는 바이트를 사람이 읽을 수 있는 문자로 변환해주는 역할을 한다
        * new BufferedReader를 통해 번역된 문자들을 한 줄씩 편하게 읽어올 수 있도록 하는 도구(버퍼)를 사용한다
        *
        * 여기서 사용된 주요 자바 문법은 [데코레이터 패턴(감싸기 구조)] 이다. 자바 입출력에서 자주 쓰이며,
        * 1. 객체의 중첩 생성(new 키웓의 연속적 사용) - 작은 기능을 가진 객체를 만들고, 더 큰 기능을 덧씌우고, 최종적으로 편리한 기능을 가진 객체로 감싸서 완성
        * 2. 반환 타입과 입력값 전달 - 안쪽의 메서드 결과물을 바깥의 생성자의 매개변수로 그대로 전달하여 데이터 연동
        * 이 있다.*/

        String response = br.lines().collect(Collectors.joining());
        /*BufferReader로 읽어들인 여러 줄의 JSON을 끊김없이 하나의 커다란 String으로 모아주는 코드..
        * - br.lines로 버퍼에 담긴 데이터들을 자바의 스트림 형태로 연속해서 읽어오고
        * - .collect(Collectors.joining())으로 흘러나오는 line들을 하나로 합쳐서 최종적으로 하나의 문자열 통째로 수집한다.*/

        br.close();
        //입출력 통로는 항상 닫아주어야 한다 (BufferedReader, InputStream처럼..)

        ObjectMapper mapper = new ObjectMapper();
        ExRateData data = mapper.readValue(response, ExRateData.class);
        //response 변수에 들어있는 JSON 문자열을 읽어서, 우리가 지정한 ExRateData 클래스의 객체로 변환해주는 코드

        BigDecimal exRate = data.rates().get("KRW");

        //2. 금액 계산
        BigDecimal convertedAmount = foreignCurrencyAmount.multiply(exRate);

        //3. 유효 시간 계산
        LocalDateTime validUntil = LocalDateTime.now().plusMinutes(30);


        return new Payment(orderId, currency, foreignCurrencyAmount, exRate, convertedAmount,
                validUntil);

        /* BigDecimal.ZERO를 사용하는 이유
        : 특별히 넣어야 할 데이터나 복잡한 계산 값이 아직 없는 상태에서, 금액 필드에 일단 0이라는 값이 잘 들어가는지 확인해보기 위함
        : 스태틱 상수의 편리함 - 자바의 BigDecimal에서는 0이나 1같은 자주 쓰는 기본적인 값들을 BigDecimal.ZERO처럼 스태틱 상수로 미리 정의해두고 있음.
        -> BigDecimal은 불변 객체이므로 여러 군데에서 똑같이 사용해도 문제가 없어서 0을 표현할 때 안전하고 편리하게 가져다 쓸 수 있음
        */
    }

    public static void main(String[] args) throws IOException {
        PaymentService paymentService = new PaymentService();
        Payment payment = paymentService.prepare(100L, "USD", BigDecimal.valueOf(50.7));
        System.out.println(payment);
    }
}
