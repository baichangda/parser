package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_bean_list;
import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_num_array;
import cn.bcd.parser.base.anno.NumType;

public class CanDataUpload implements PacketBody {
    //数据项个数
    @F_num(type = NumType.uint16,var = 'n')
    public int num;
    //can总线数据接收时间
    @F_num_array(singleType = NumType.uint8, len = 5)
    public byte[] time;
    //can总线数据项
    @F_bean_list(listLenExpr = "n")
    public CanDataItem[] items;
}
