package cn.bcd.parser.impl.jtt808.data;

import cn.bcd.parser.anno.F_num;
import cn.bcd.parser.anno.F_num_array;
import cn.bcd.parser.anno.NumType;

public class CanDataItem {
    //数据项个数
    @F_num(type = NumType.uint32)
    public long id;
    //can数据
    @F_num_array(singleType = NumType.uint8, len = 8)
    public byte[] data;
}