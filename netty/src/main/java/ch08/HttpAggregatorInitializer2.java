package ch08;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpAggregatorInitializer2 extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpAggregatorInitializer2(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec())
                    .addLast("decompressor", new HttpContentDecompressor());
        }
        else {
            pipeline.addLast("codec", new HttpServerCodec())
                    .addLast("decompressor", new HttpContentDecompressor());
        }
    }

}
