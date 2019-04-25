import org.junit.Test;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author paul
 * @description
 * @date 2019/3/19
 */
public class WebclientTest {

    @Test
    public void test1(){
        WebClient webClient = WebClient.create("http://127.0.0.1:89");
        Mono<ClientResponse> exchange = webClient.get().uri("/serviced/name").exchange();
        ClientResponse block = exchange.block();
        ClientResponse.Headers headers = block.headers();
        System.out.println(headers);

    }
}
