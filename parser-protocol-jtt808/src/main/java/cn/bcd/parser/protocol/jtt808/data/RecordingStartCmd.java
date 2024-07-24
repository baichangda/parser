package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.NumType;

public class RecordingStartCmd implements PacketBody{
    //录音命令
    @F_num(type = NumType.uint8)
    public byte cmd;
    //录音时间
    @F_num(type = NumType.uint16)
    public int time;
    //保存标志
    @F_num(type = NumType.uint8)
    public byte flag;
    //音频采样率
    @F_num(type = NumType.uint8)
    public byte samplingRate;
}
