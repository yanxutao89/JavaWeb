package NettyInAction.ch12;


public class SpdyRequestHandler extends HttpRequestHandler2 {

    @Override
    protected String getContent() {
        return "This content is transmitted via SPDY\r\n";
    }

}
