package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.data.NumType;

public class CameraTakePhotoCmdRequest implements PacketBody{
    //通道id
    @F_num(type = NumType.uint8)
    public short id;
    //拍摄命令
    @F_num(type = NumType.uint16)
    public int cmd;
    //拍照间隔/录像时间
    @F_num(type = NumType.uint16)
    public int period;
    //保存标志
    @F_num(type = NumType.uint8)
    public byte flag;
    //分辨率
    @F_num(type = NumType.uint8)
    public short resolution;
    //图像/视频质量
    @F_num(type = NumType.uint8)
    public byte quality;
    //亮度
    @F_num(type = NumType.uint8)
    public short brightness;
    //对比度
    @F_num(type = NumType.uint8)
    public byte contrast;
    //饱和度
    @F_num(type = NumType.uint8)
    public byte saturation;
    //色度
    @F_num(type = NumType.uint8)
    public short chroma;
}
