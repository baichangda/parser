package cn.bcd.parser.builder;

import cn.bcd.parser.anno.F_date_bytes_7;
import cn.bcd.parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FieldBuilder__F_date_bytes_7 extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final F_date_bytes_7 anno = context.field.getAnnotation(F_date_bytes_7.class);
        final Field field = context.field;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final Class<?> fieldTypeClass = field.getType();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final String varNameZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        //先转换为毫秒
        final String readFuncName = bigEndian ? "readUnsignedShort" : "readUnsignedShortLE";
        ParseUtil.append(body, "final long {}={}.of({}.{}(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant().toEpochMilli();\n",
                varNameLongField, zoneDateTimeClassName
                , varNameByteBuf, readFuncName, varNameByteBuf, varNameByteBuf, varNameByteBuf
                , varNameByteBuf, varNameByteBuf, varNameZoneId);

        //根据字段类型格式化
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            final String dateClassName = Date.class.getName();
            ParseUtil.append(body, "{}.{}=new {}({});\n", varNameInstance, field.getName(), dateClassName, varNameLongField);
        } else if (Instant.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "{}.{}={}.ofEpochMilli({});\n", varNameInstance, field.getName(), Instant.class.getName(), varNameLongField);
        } else if (LocalDateTime.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameValueZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.valueZoneId());
            ParseUtil.append(body, "{}.{}={}.ofInstant({}.ofEpochMilli({}),{});\n", varNameInstance, field.getName(), LocalDateTime.class.getName(), Instant.class.getName(), varNameLongField, varNameValueZoneId);
        } else if (OffsetDateTime.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameValueZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.valueZoneId());
            ParseUtil.append(body, "{}.{}={}.ofInstant({}.ofEpochMilli({}),{});\n", varNameInstance, field.getName(), OffsetDateTime.class.getName(), Instant.class.getName(), varNameLongField, varNameValueZoneId);
        } else if (ZonedDateTime.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameValueZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.valueZoneId());
            ParseUtil.append(body, "{}.{}={}.ofInstant({}.ofEpochMilli({}),{});\n", varNameInstance, field.getName(), ZonedDateTime.class.getName(), Instant.class.getName(), varNameLongField, varNameValueZoneId);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameLongField);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "{}.{}=(int)({}/1000);\n", varNameInstance, field.getName(), varNameLongField);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameValueZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.valueZoneId());
            final String dateTimeFormatterVarName = ParseUtil.defineClassVar(context, DateTimeFormatter.class, "{}.ofPattern(\"{}\").withZone({})", DateTimeFormatter.class.getName(), anno.stringFormat(), varNameValueZoneId);
            ParseUtil.append(body, "{}.{}={}.ofInstant({}.ofEpochMilli({}),{}).format({});\n",
                    varNameInstance,
                    field.getName(),
                    zoneDateTimeClassName,
                    Instant.class.getName(),
                    varNameLongField,
                    varNameValueZoneId,
                    dateTimeFormatterVarName);
        } else {
            ParseUtil.notSupport_fieldType(context, F_date_bytes_7.class);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.method_body;
        final F_date_bytes_7 anno = context.field.getAnnotation(F_date_bytes_7.class);
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String valCode = varNameInstance + "." + field.getName();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        //根据字段类型获取long
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}={}.getTime();\n", varNameLongField, valCode);
        } else if (Instant.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}={}.toEpochMilli();\n", varNameLongField, valCode);
        } else if (LocalDateTime.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameValueZoneOffset = ParseUtil.defineClassVar(context, ZoneOffset.class, "{}.of(\"{}\")", ZoneOffset.class.getName(), anno.valueZoneId());
            ParseUtil.append(body, "final long {}={}.toInstant({}).toEpochMilli();\n", varNameLongField, valCode, varNameValueZoneOffset);
        } else if (OffsetDateTime.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}={}.toInstant().toEpochMilli();\n", varNameLongField, valCode);
        } else if (ZonedDateTime.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}={}.toInstant().toEpochMilli();\n", varNameLongField, valCode);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}={};\n", varNameLongField, valCode);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}=(long)({})*1000L;\n", varNameLongField, valCode);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameValueZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.valueZoneId());
            final String dateTimeFormatterVarName = ParseUtil.defineClassVar(context, DateTimeFormatter.class, "{}.ofPattern(\"{}\").withZone({})", DateTimeFormatter.class.getName(), anno.stringFormat(), varNameValueZoneId);
            ParseUtil.append(body, "final long {}={}.parse({},{}).toInstant().toEpochMilli();\n", varNameLongField, zoneDateTimeClassName, valCode, dateTimeFormatterVarName);
        } else {
            ParseUtil.notSupport_fieldType(context, F_date_bytes_7.class);
        }

        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final String writeFuncName = bigEndian ? "writeShort" : "writeShortLE";
        final String varNameZoneDateTimeField = varNameField + "zoneDateTime";
        ParseUtil.append(body, "{} {}={}.ofInstant({}.ofEpochMilli({}),{});\n",
                zoneDateTimeClassName,
                varNameZoneDateTimeField,
                zoneDateTimeClassName,
                Instant.class.getName(),
                varNameLongField,
                varNameZoneId);
        ParseUtil.append(body, "{}.{}((short){}.getYear());\n", varNameByteBuf, writeFuncName, varNameZoneDateTimeField);
        ParseUtil.append(body, "{}.writeBytes(new byte[]{(byte)({}.getMonthValue()),(byte)({}.getDayOfMonth()),(byte)({}.getHour()),(byte)({}.getMinute()),(byte)({}.getSecond())});\n",
                varNameByteBuf,
                varNameZoneDateTimeField,
                varNameZoneDateTimeField,
                varNameZoneDateTimeField,
                varNameZoneDateTimeField,
                varNameZoneDateTimeField);
    }

}
