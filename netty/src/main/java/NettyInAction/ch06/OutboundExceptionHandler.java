package NettyInAction.ch06;

import io.netty.channel.*;

/**
 * Listing 6.14 Adding a ChannelFutureListener to a ChannelPromise
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg,
        ChannelPromise promise) {
        promise.addListener((ChannelFuture f) -> {
            if (!f.isSuccess()) {
                f.cause().printStackTrace();
                f.channel().close();
            }
        });
    }

}
