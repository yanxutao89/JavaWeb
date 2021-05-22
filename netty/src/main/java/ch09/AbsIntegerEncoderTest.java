package ch09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class AbsIntegerEncoderTest {

    @Test
    public void testEncoded() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 1; i < 10; ++i) {
            buffer.writeInt(i * -1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        Assert.assertTrue(channel.writeOutbound(buffer));

        Assert.assertTrue(channel.finish());

        // read bytes
//        ByteBuf output = channel.readOutbound();
        for (int i = 1; i < 10; ++i) {
            Assert.assertEquals(i, (int)channel.readOutbound());
        }
//        Assert.assertFalse(output.isReadable());
        Assert.assertNull(channel.readOutbound());
    }

}
