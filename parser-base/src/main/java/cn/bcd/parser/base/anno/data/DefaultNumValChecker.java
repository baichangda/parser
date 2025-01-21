package cn.bcd.parser.base.anno.data;

public class DefaultNumValChecker extends NumValGetter {
    public final static DefaultNumValChecker instance = new DefaultNumValChecker();
    private DefaultNumValChecker(){

    }
    @Override
    public int getType(NumType numType, int val) {
        return switch (numType) {
            case uint8, int8 -> switch (val & 0xFF) {
                case 0xFF -> 1;
                case 0xFE -> 2;
                default -> 0;
            };
            case uint16, int16 -> switch (val & 0xFFFF) {
                case 0xFFFF -> 1;
                case 0xFFFE -> 2;
                default -> 0;
            };
            case uint24, int24 -> switch (val & 0xFFFFFF) {
                case 0xFFFFFF -> 1;
                case 0xFFFFFE -> 2;
                default -> 0;
            };
            case uint32, int32 -> switch (val) {
                case 0xFFFFFFFF -> 1;
                case 0xFFFFFFFE -> 2;
                default -> 0;
            };
            default -> 0;
        };
    }

    @Override
    public int getType(NumType numType, long val) {
        switch (numType) {
            case uint40, int40 -> {
                if (val == 0xFFFFFFFFFFL) {
                    return 1;
                } else if (val == 0xFFFFFFFFFEL) {
                    return 2;
                } else {
                    return 0;
                }
            }
            case uint48, int48 -> {
                if (val == 0xFFFFFFFFFFFFL) {
                    return 1;
                } else if (val == 0xFFFFFFFFFFFEL) {
                    return 2;
                } else {
                    return 0;
                }
            }
            case uint56, int56 -> {
                if (val == 0xFFFFFFFFFFFFFFL) {
                    return 1;
                } else if (val == 0xFFFFFFFFFFFFFEL) {
                    return 2;
                } else {
                    return 0;
                }
            }
            case uint64, int64 -> {
                if (val == 0xFFFFFFFFFFFFFFFFL) {
                    return 1;
                } else if (val == 0xFFFFFFFFFFFFFFFEL) {
                    return 2;
                } else {
                    return 0;
                }
            }
            default -> {
                return 0;
            }
        }
    }

    @Override
    public int getVal_int(NumType numType, int type) {
        return switch (numType) {
            case uint8, int8 -> switch (type) {
                case 1 -> 0xFF;
                case 2 -> 0xFE;
                default -> 0;
            };
            case uint16, int16 -> switch (type) {
                case 1 -> 0xFFFF;
                case 2 -> 0xFFFE;
                default -> 0;
            };
            case uint24, int24 -> switch (type) {
                case 1 -> 0xFFFFFF;
                case 2 -> 0xFFFFFE;
                default -> 0;
            };
            case uint32, int32 -> switch (type) {
                case 1 -> 0xFFFFFFFF;
                case 2 -> 0xFFFFFFFE;
                default -> 0;
            };
            default -> 0;
        };
    }

    @Override
    public long getVal_long(NumType numType, int type) {
        return switch (numType) {
            case uint40, int40 -> switch (type) {
                case 1 -> 0xFFFFFFFFFFL;
                case 2 -> 0xFFFFFFFFFEL;
                default -> 0;
            };
            case uint48, int48 -> switch (type) {
                case 1 -> 0xFFFFFFFFFFFFL;
                case 2 -> 0xFFFFFFFFFFFEL;
                default -> 0;
            };
            case uint56, int56 -> switch (type) {
                case 1 -> 0xFFFFFFFFFFFFFFL;
                case 2 -> 0xFFFFFFFFFFFFFEL;
                default -> 0;
            };
            case uint64, int64 -> switch (type) {
                case 1 -> 0xFFFFFFFFFFFFFFFFL;
                case 2 -> 0xFFFFFFFFFFFFFFFEL;
                default -> 0;
            };
            default -> 0;
        };
    }

    public static void main(String[] args) {
        DefaultNumValChecker defaultNumValChecker = new DefaultNumValChecker();
        System.out.println(defaultNumValChecker.getType(NumType.uint8, (byte) 0xFF));
    }
}
