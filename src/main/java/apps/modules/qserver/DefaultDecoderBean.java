package apps.modules.qserver;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultDecoderBean {
    private byte flag1;
    private byte flag2;
    private int cmd;
    private int version;
    private int datatype;
    private int reserved;
    private int length;
    private ByteBuf byteBuf;
    
    public DefaultDecoderBean(byte flag1, byte flag2, int cmd, int version, 
    		int datatype, int reserved, int length, ByteBuf byteBuf) {
    	this.flag1 = flag1;
    	this.flag2 = flag2;
    	this.cmd = cmd;
    	this.version = version;
    	this.datatype = datatype;
    	this.reserved = reserved;
    	this.length = length;
    	this.byteBuf = byteBuf;
    }
}