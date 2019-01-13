package apps.modules.qserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
//import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.alibaba.fastjson.util.IOUtils;

@Slf4j
public class DefaultDecoder  extends LengthFieldBasedFrameDecoder {
	
	private static final int HEADER_SIZE = 14;
	
    public DefaultDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, 
    		int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
    	super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if(in == null){
            return null;
        }
    	
        if(in.readableBytes() < HEADER_SIZE){
            return null;
        }
        
        byte flag1 = in.readByte();
        byte flag2 = in.readByte();
        if(flag1 != 84 || flag2 != 84) {
        	log.error("head长度不够");
        	return null;
        }
        short cmd = (short) in.readUnsignedShortLE();
        int version = in.readByte();
        if(version != 1) {
        	log.error("head version != 1");
        	return null;
        }
        int datatype = in.readByte();
        if(datatype != 1) {
        	log.error("head datatype != 1");
        	return null;
        }
        int reserved = (int) in.readUnsignedIntLE();
        int length = (int) in.readUnsignedIntLE();
        
        if(in.readableBytes() != length){
        	log.error("bytes跟长度不一致");
        	return null;
        }
        byte []bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        
    	ByteBuf tempByteBuf = Unpooled.buffer(in.readableBytes());
    	tempByteBuf.writeBytes(bytes);
        return new DefaultDecoderBean(flag1, flag2, cmd, version, datatype, reserved, length, tempByteBuf);
    }
}
