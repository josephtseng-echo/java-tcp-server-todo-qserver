package apps.modules.qserver;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier("defaultChannelInitializer")
public class DefaultChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    @Qualifier("defaultServerHandler")
    private ChannelInboundHandlerAdapter defaultServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //TT 协议
        pipeline.addLast(new DefaultDecoder(1024*1024, 10, 4, 0, 0, false));
        pipeline.addLast(new DefaultEncoder());
        pipeline.addLast(defaultServerHandler);
    }
}