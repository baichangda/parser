package cn.bcd.parser.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BitUtil {

    public static class Signal {
        public final String name;
        public final int startBit;
        public final int length;

        //计算值
        //根据startBit和length计算出结束位
        public int calc_endBit;
        //根据calc_endBit计算出的翻转的起始位
        public int calc_startBit_reverse;
        //根据calc_startBit_reverse得出字段占用的位数
        public int calc_skip_before;

        public Signal(String name, int startBit, int length) {
            this.name = name;
            this.startBit = startBit;
            this.length = length;
        }

        @Override
        public String toString() {
            return "Signal{" +
                    "name='" + name + '\'' +
                    ", calc_skip_before=" + calc_skip_before +
                    ", length=" + length +
                    ", startBit=" + startBit +
                    ", calc_endBit=" + calc_endBit +
                    ", calc_startBit_reverse=" + calc_startBit_reverse +
                    '}';
        }
    }

    /**
     * 按照CAN矩阵 Motorola_LSB 信号规则
     * 计算出配合{@link cn.bcd.parser.base.anno.F_bit_num}使用的相应属性
     * @param signals
     * @return
     */
    public static Signal[] calc_Motorola_LSB(Signal[] signals) {
        ArrayList<Signal> list = new ArrayList<>();
        //计算结束位、翻转起始位
        for (Signal signal : signals) {
            int row = signal.startBit / 8;
            int rowStartIndex = row * 8 + 7;
            int rowLeave = rowStartIndex - signal.startBit + 1;
            int endBit;
            if (rowLeave < signal.length) {
                int n1 = signal.length - rowLeave;
                int n2 = n1 / 8;
                int n3 = n1 % 8;
                int endRow = row - n2;
                if (n3 > 0) {
                    endRow -= 1;
                }
                endBit = endRow * 8 + n3;
            } else {
                endBit = signal.startBit + signal.length;
            }
            signal.calc_endBit = endBit - 1;
            signal.calc_startBit_reverse = (signal.calc_endBit / 8 + 1) * 8 - (signal.calc_endBit % 8) - 1;
            list.add(signal);
        }

        //排序后计算skip
        list.sort(Comparator.comparing(e -> e.calc_startBit_reverse));
        int pos = 0;
        for (Signal signal : list) {
            int skip = signal.calc_startBit_reverse - pos;
            if (skip > 0) {
                signal.calc_skip_before = skip;
                pos += skip;
            }
            pos += signal.length;
        }

        return list.toArray(new Signal[0]);
    }

    public static void main(String[] args) {
        Signal[] res = calc_Motorola_LSB(new Signal[]{
//                new Signal("TBOX_RemoteAuthRequest", 0, 2),
//                new Signal("TBOX_RemoteControlDoor", 8, 2),
//                new Signal("TBOX_RemoteControlWindow", 10, 3),
//                new Signal("TBOX_RemoteSearchCar", 13, 2),
//                new Signal("TBOX_RemoteCtrlSigSrc", 16, 2),
//                new Signal("TBOX_OTAModeRequest", 20, 2),
//                new Signal("TBOX_RemoteControlAC", 24, 4),
//                new Signal("TBOX_RemoteACStartTime", 28, 3),
//                new Signal("TBOX_RemoteACTempSet", 32, 6),
//                new Signal("TBOX_RemoteControlSeatHeat", 40, 3),
//                new Signal("TBOX_RemoteSeatHeatTime", 43, 3),
//                new Signal("TBOX_RemoteStart", 51, 2),
//                new Signal("TBOX_RemoteStartActiveTime", 53, 3),
                new Signal("test1", 13, 2),
                new Signal("test2", 37, 13),
                new Signal("test2", 62, 7),
        });
        Arrays.sort(res, Comparator.comparing(e -> e.calc_startBit_reverse));
        for (Signal signal : res) {
            System.out.println(signal);
        }
    }
}
