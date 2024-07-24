package cn.bcd.parser.protocol.jtt808.data;

public enum TerminalParamId {
    terminal_heartbeat_interval(0x0001, "终端心跳发送间隔,单位为秒(s)"),
    tcp_msg_response_timeout(0x0002, "TCP消息应答超时时间,单位为秒(s)"),
    tcp_msg_retransmissions_count(0x0003, "TCP消息重传次数"),
    udp_msg_response_timeout(0x0004, "UDP消息应答超时时间,单位为秒(s)"),
    udp_msg_retransmissions_count(0x0005, "UDP消息重传次数"),
    sms_msg_response_timeout(0x0006, "SMS消息应答超时时间,单位为秒(s)"),
    sms_msg_retransmissions_count(0x0007, "SMS消息重传次数"),
    ;
    public final int id;
    public final String remark;

    TerminalParamId(int id, String remark) {
        this.id = id;
        this.remark = remark;
    }
}
