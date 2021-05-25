package NettyInAction.ch07;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.WebSocketFrame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        switch (msg.getType()) {
            case BINARY:
                out.add(new BinaryWebSocketFrame(msg.getData()));
                return;
            case TEXT:
                out.add(new TextWebSocketFrame(msg.getData()));
                return;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, msg.getData()));
                return;
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(msg.getData()));
                return;
            case PING:
                out.add(new PingWebSocketFrame(msg.getData()));
                return;
            case PONG:
                out.add(new PongWebSocketFrame(msg.getData()));
                return;
            default:
                throw new IllegalStateException("Unsupported webSocket msg " + msg);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, io.netty.handler.codec.http.websocketx.WebSocketFrame msg, List<Object> out) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new WebSocketFrame(
                    WebSocketFrame.FrameType.BINARY, msg.copy().content()
            ));
        }
        if (msg instanceof TextWebSocketFrame) {
            out.add(new WebSocketFrame(
                    WebSocketFrame.FrameType.TEXT, msg.copy().content()
            ));
        }
        if (msg instanceof CloseWebSocketFrame) {
            out.add(new WebSocketFrame(
                    WebSocketFrame.FrameType.CLOSE, msg.copy().content()
            ));
        }
        if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new WebSocketFrame(
                    WebSocketFrame.FrameType.CONTINUATION, msg.copy().content()
            ));
        }
        if (msg instanceof PingWebSocketFrame) {
            out.add(new WebSocketFrame(
                    WebSocketFrame.FrameType.PING, msg.copy().content()
            ));
        }
        if (msg instanceof PongWebSocketFrame) {
            out.add(new WebSocketFrame(
                    WebSocketFrame.FrameType.PONG, msg.copy().content()
            ));
        }
        throw new IllegalStateException("Unsupported webSocket msg " + msg);
    }

    public static final class WebSocketFrame {

        public enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf data;

        public WebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }
    }

}
