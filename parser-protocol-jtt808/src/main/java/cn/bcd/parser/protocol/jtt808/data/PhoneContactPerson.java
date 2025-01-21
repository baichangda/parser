package cn.bcd.parser.protocol.jtt808.data;

import cn.bcd.parser.base.anno.F_num;
import cn.bcd.parser.base.anno.F_string;
import cn.bcd.parser.base.anno.data.NumType;

public class PhoneContactPerson {
    //标志
    @F_num(type = NumType.uint8)
    public byte flag;
    //号码长度
    @F_num(type = NumType.uint8, var = 'a')
    public short phoneNumberLen;
    //电话号码
    @F_string(lenExpr = "a", charset = "GBK")
    public String phoneNumber;
    //联系人长度
    @F_num(type = NumType.uint8, var = 'b')
    public short concatPersonLen;
    //联系人
    @F_string(lenExpr = "b", charset = "GBK")
    public String concatPerson;
}
