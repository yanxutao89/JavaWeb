package NettyInAction.ch14;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;


public class MemcachedRequestEncoder extends MessageToByteEncoder<MemcachedRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MemcachedRequest msg, ByteBuf out) throws Exception {
        // convert key and body to bytes array
        byte[] key = msg.getKey().getBytes(CharsetUtil.UTF_8);
        byte[] body = msg.getBody().getBytes(CharsetUtil.UTF_8);
        // total size of body = key size + content size + extras size
        int bodySize = key.length + body.length + (msg.isHasExtras() ? 8 : 0);

        // write magic byte
        out.writeByte(msg.getMagic());
        // write opCode byte
        out.writeByte(msg.getOpCode());
        // write key length (2 byte) i.e a Java short
        out.writeShort(key.length);
        // write extras length (1 byte)
        int extraSize = msg.isHasExtras() ? 0x08 : 0x0;
        out.writeByte(extraSize);
        // byte is the data type, not currently implemented iin Memcached but required
        out.writeByte(0);
        // next two bytes are reserved, not currently implemented but are required
        out.writeShort(0);

        // write total body length ( 4 bytes - 32 bit  int)
        out.writeInt(bodySize);
        // write opaue ( 4 bytes) - a 32 bit int that is returned in the response
        out.writeInt(msg.getId());

        // write CAS ( 8 bytes) 24 byte head finishes with the CAS
        out.writeLong(msg.getCas());

        if (msg.isHasExtras()) {
            // write extras (flags and expiry, 4 bytes each) - 8 bytes total
            out.writeInt(msg.getFlags());
            out.writeInt(msg.getExpires());
        }
        // write key
        out.writeBytes(key);
        // write body
        out.writeBytes(body);
    }

}
