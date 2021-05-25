package NettyInAction.ch14;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class MemcachedResponseDecoderTest {

    @Test
    public void testMemcachedResponseDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new MemcachedResponseDecoder());

        byte magic = 1;
        byte opCode = OpCode.SET;
        byte dataType = 0;

        byte[] key = "Key1".getBytes(CharsetUtil.US_ASCII);
        byte[] body = "Value".getBytes(CharsetUtil.US_ASCII);
        int id = (int) System.currentTimeMillis();
        long cas = System.currentTimeMillis();

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(magic);
        buffer.writeByte(opCode);
        buffer.writeShort(key.length);
        buffer.writeByte(0);
        buffer.writeByte(dataType);
        buffer.writeShort(Status.KEY_EXISTS);
        buffer.writeInt(key.length + body.length);
        buffer.writeInt(id);
        buffer.writeLong(cas);
        buffer.writeBytes(key);
        buffer.writeBytes(body);

        Assert.assertTrue(channel.writeInbound(buffer));

        MemcachedResponse response = channel.readInbound();
        assertResponse(response, magic, opCode, dataType,
                Status.KEY_EXISTS, 0, 0, id, cas, key, body);
    }

    @Test
    public void testMemcachedResponseDecoderFragments() {
        EmbeddedChannel channel = new EmbeddedChannel(new MemcachedResponseDecoder());

        byte magic = 1;
        byte opCode = OpCode.SET;
        byte dataType = 0;

        byte[] key = "Key1".getBytes(CharsetUtil.US_ASCII);
        byte[] body = "Value".getBytes(CharsetUtil.US_ASCII);
        int id = (int) System.currentTimeMillis();
        long cas = System.currentTimeMillis();

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(magic);
        buffer.writeByte(opCode);
        buffer.writeShort(key.length);
        buffer.writeByte(0);
        buffer.writeByte(dataType);
        buffer.writeShort(Status.KEY_EXISTS);
        buffer.writeInt(key.length + body.length);
        buffer.writeInt(id);
        buffer.writeLong(cas);
        buffer.writeBytes(key);
        buffer.writeBytes(body);

        ByteBuf fragment1 = buffer.readBytes(8);
        ByteBuf fragment2 = buffer.readBytes(24);
        ByteBuf fragment3 = buffer;

        Assert.assertFalse(channel.writeInbound(fragment1));
        Assert.assertFalse(channel.writeInbound(fragment2));
        Assert.assertTrue(channel.writeInbound(fragment3));

        MemcachedResponse response = channel.readInbound();
        assertResponse(response, magic, opCode, dataType,
                Status.KEY_EXISTS, 0, 0, id, cas, key, body);
    }

    private static void assertResponse(MemcachedResponse response, byte magic, byte opCode, byte dataType, short status, int expires, int flags, int id, long cas, byte[] key, byte[] body) {
        Assert.assertEquals(magic, response.getMagic());
        Assert.assertArrayEquals(key, response.getKey().getBytes(CharsetUtil.US_ASCII));
        Assert.assertEquals(opCode, response.getOpCode());
        Assert.assertEquals(dataType, response.getDataType());
        Assert.assertEquals(status, response.getStatus());
        Assert.assertEquals(cas, response.getCas());
        Assert.assertEquals(expires, response.getExpires());
        Assert.assertEquals(flags, response.getFlags());
        Assert.assertArrayEquals(body, response.getData().getBytes(CharsetUtil.US_ASCII));
        Assert.assertEquals(id, response.getId());
    }


}
