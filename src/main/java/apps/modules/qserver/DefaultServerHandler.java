package apps.modules.qserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
//import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;

import apps.modules.netty.ChannelRepository;


@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class DefaultServerHandler extends ChannelInboundHandlerAdapter {

    private final ChannelRepository channelRepository;

    /**
     * 建立连接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");
        ctx.fireChannelActive();
        if (log.isDebugEnabled()) {
            log.debug(ctx.channel().remoteAddress() + "");
        }
        String channelKey = ctx.channel().remoteAddress().toString();
        channelRepository.put(channelKey, ctx.channel());
        log.info("client channel key active:" + channelKey);
        if (log.isDebugEnabled()) {
            log.debug("binded Channel Count is {}", this.channelRepository.size());
        }
    }
    
    /**
     * 心跳包
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    	//TODO
        super.userEventTriggered(ctx, evt);
    }
    
    /**
     * 读取数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	DefaultDecoderBean defaultDecodeBean = (DefaultDecoderBean)msg;
    	ByteBuf  byteBuf = defaultDecodeBean.getByteBuf();
    	
    	byte[] bytes = new byte[byteBuf.readIntLE()];  
    	byteBuf.readBytes(bytes);  
        String result = new String(bytes);
        
        //json data TODO
        if(defaultDecodeBean.getDatatype() == 1) {
        	try {
        		JSONObject resultJson = JSONObject.parseObject(result);
        		log.info("resultJson = " + resultJson);
        	} catch (Exception e) {
        		//TODO
        	}
        }
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	    // TODO
	    super.channelReadComplete(ctx);
	    ctx.flush();
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
    }

    /**
     * 关闭连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");
        Assert.notNull(ctx, "[Assertion failed] - ChannelHandlerContext is required; it must not be null");
        String channelKey = ctx.channel().remoteAddress().toString();
        this.channelRepository.remove(channelKey);
        log.info("client channel key remove:" + channelKey);
        if (log.isDebugEnabled()) {
            log.debug("binded Channel Count is " + this.channelRepository.size());
        }
    }
}
