package NettyInAction.ch08;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 8.1 Bootstrapping a client
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
public class BootstrapClient {

    public static void main(String args[]) {
        BootstrapClient client = new BootstrapClient();
        client.bootstrap();
    }

    /**
     * Listing 8.1 Bootstrapping a client
     * */
    public void bootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                @Override
                public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                    super.channelRegistered(ctx);
                    System.out.println("Channel registered");
                }

                @Override
                protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                    System.out.println("Received data");
                }

                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    super.channelActive(ctx);
                    System.out.println("Channel active");
                }

                @Override
                public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                    super.channelUnregistered(ctx);
                    System.out.println("Channel unregistered");
                }

                @Override
                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                    super.channelInactive(ctx);
                    System.out.println("Channel inactive");
                }
            });
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
        future.addListener((ChannelFuture channelFuture) -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Connection established");
            } else {
                System.err.println("Connection attempt failed");
                channelFuture.cause().printStackTrace();
            }
        });
    }
}
