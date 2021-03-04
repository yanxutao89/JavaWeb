//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//
///**
// * @Author: Yanxt7
// * @Desc:
// * @Date: 2021/03/04 13:55
// */
//@ExtendWith(SpringExtension.class)
////  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class GreetingRouterTest {
//
//	// Spring Boot will create a `WebTestClient` for you,
//	// already configure and ready to issue requests against "localhost:RANDOM_PORT"
//	@Autowired
//	private WebTestClient webTestClient;
//
//	@Test
//	public void testHello() {
//		webTestClient
//				// Create a GET request to test an endpoint
//				.get().uri("/hello")
//				.accept(MediaType.TEXT_PLAIN)
//				.exchange()
//				// and use the dedicated DSL to test assertions against the response
//				.expectStatus().isOk()
//				.expectBody(String.class).isEqualTo("Hello, Spring!");
//	}
//}
