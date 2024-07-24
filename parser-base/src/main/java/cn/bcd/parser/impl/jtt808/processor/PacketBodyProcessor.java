package cn.bcd.parser.impl.jtt808.processor;

import cn.bcd.parser.Parser;
import cn.bcd.parser.exception.ParseException;
import cn.bcd.parser.impl.jtt808.data.*;
import cn.bcd.parser.processor.ProcessContext;
import cn.bcd.parser.processor.Processor;
import io.netty.buffer.ByteBuf;

public class PacketBodyProcessor implements Processor<PacketBody> {
    Processor<TerminalCommonResponse> processor_TerminalCommonResponse = Parser.getProcessor(TerminalCommonResponse.class);

    Processor<PlatformCommonResponse> processor_PlatformCommonResponse = Parser.getProcessor(PlatformCommonResponse.class);

    Processor<QueryServerTimeResponse> processor_QueryServerTimeResponse = Parser.getProcessor(QueryServerTimeResponse.class);

    Processor<ServerSubPacketRequest> processor_ServerSubPacketRequest = Parser.getProcessor(ServerSubPacketRequest.class);

    Processor<TerminalAuthentication> processor_TerminalAuthentication = Parser.getProcessor(TerminalAuthentication.class);

    Processor<SetTerminalParam> processor_SetTerminalParam = Parser.getProcessor(SetTerminalParam.class);

    Processor<QueryTerminalParamRequest> processor_QueryTerminalParamRequest = Parser.getProcessor(QueryTerminalParamRequest.class);

    Processor<QueryTerminalParamResponse> processor_QueryTerminalParamResponse = Parser.getProcessor(QueryTerminalParamResponse.class);

    Processor<QueryTerminalPropResponse> processor_QueryTerminalPropResponse = Parser.getProcessor(QueryTerminalPropResponse.class);

    Processor<IssuedTerminalUpgradeRequest> processor_IssuedTerminalUpgradeRequest = Parser.getProcessor(IssuedTerminalUpgradeRequest.class);

    Processor<TerminalUpgradeResResponse> processor_TerminalUpgradeResResponse = Parser.getProcessor(TerminalUpgradeResResponse.class);
    Processor<TempPositionFollow> processor_TempPositionFollow = Parser.getProcessor(TempPositionFollow.class);
    Processor<ConfirmAlarmMsg> processor_ConfirmAlarmMsg = Parser.getProcessor(ConfirmAlarmMsg.class);
    Processor<SetPhoneText> processor_SetPhoneText = Parser.getProcessor(SetPhoneText.class);
    Processor<DeleteCircleArea> processor_DeleteCircleArea = Parser.getProcessor(DeleteCircleArea.class);
    Processor<DeleteRectangleArea> processor_DeleteRectangleArea = Parser.getProcessor(DeleteRectangleArea.class);
    Processor<DeletePolygonArea> processor_DeletePolygonArea = Parser.getProcessor(DeletePolygonArea.class);
    Processor<DeletePath> processor_DeletePath = Parser.getProcessor(DeletePath.class);
    Processor<QueryAreaOrPathRequest> processor_QueryAreaOrPathRequest = Parser.getProcessor(QueryAreaOrPathRequest.class);
    Processor<WaybillReport> processor_WaybillReport = Parser.getProcessor(WaybillReport.class);
    Processor<CanDataUpload> processor_CanDataUpload = Parser.getProcessor(CanDataUpload.class);
    Processor<MultiMediaEventUpload> processor_MultiMediaEventUpload = Parser.getProcessor(MultiMediaEventUpload.class);
    Processor<CameraTakePhotoCmdRequest> processor_CameraTakePhotoCmdRequest = Parser.getProcessor(CameraTakePhotoCmdRequest.class);
    Processor<CameraTakePhotoCmdResponse> processor_CameraTakePhotoCmdResponse = Parser.getProcessor(CameraTakePhotoCmdResponse.class);
    Processor<StorageMultiMediaDataFetchRequest> processor_StorageMultiMediaDataFetchRequest = Parser.getProcessor(StorageMultiMediaDataFetchRequest.class);
    Processor<StorageMultiMediaDataUploadCmd> processor_StorageMultiMediaDataUploadCmd = Parser.getProcessor(StorageMultiMediaDataUploadCmd.class);
    Processor<RecordingStartCmd> processor_RecordingStartCmd = Parser.getProcessor(RecordingStartCmd.class);

    Processor<SingleMultiMediaDataFetchUploadCmd> processor_SingleMultiMediaDataFetchUploadCmd = Parser.getProcessor(SingleMultiMediaDataFetchUploadCmd.class);
    Processor<PlatformRsa> processor_PlatformRsa = Parser.getProcessor(PlatformRsa.class);
    Processor<TerminalRsa> processor_TerminalRsa = Parser.getProcessor(TerminalRsa.class);


    @Override
    public PacketBody process(ByteBuf data, ProcessContext<?> processContext) {
        Packet packet = (Packet) processContext.instance;
        PacketBody packetBody;
        switch (packet.header.msgId) {
            case 0x0001 -> {
                packetBody = processor_TerminalCommonResponse.process(data, processContext);
            }
            case 0x8001 -> {
                packetBody = processor_PlatformCommonResponse.process(data, processContext);
            }
            case 0x0002, 0x0004, 0x0003, 0x8104, 0x8107, 0x8201, 0x8204, 0x8702 -> {
                packetBody = null;
            }
            case 0x8004 -> {
                packetBody = processor_QueryServerTimeResponse.process(data, processContext);
            }
            case 0x8003, 0x0005 -> {
                packetBody = processor_ServerSubPacketRequest.process(data, processContext);
            }
            case 0x0100 -> {
                packetBody = TerminalRegisterRequest.read(data, packet.header.msgLen);
            }
            case 0x8100 -> {
                packetBody = TerminalRegisterResponse.read(data, packet.header.msgLen);
            }
            case 0x0102 -> {
                packetBody = processor_TerminalAuthentication.process(data, processContext);
            }
            case 0x8103 -> {
                packetBody = processor_SetTerminalParam.process(data, processContext);
            }
            case 0x8106 -> {
                packetBody = processor_QueryTerminalParamRequest.process(data, processContext);
            }
            case 0x0104 -> {
                packetBody = processor_QueryTerminalParamResponse.process(data, processContext);
            }
            case 0x8105 -> {
                packetBody = TerminalControl.read(data, packet.header.msgLen);
            }
            case 0x0107 -> {
                packetBody = processor_QueryTerminalPropResponse.process(data, processContext);
            }
            case 0x8108 -> {
                packetBody = processor_IssuedTerminalUpgradeRequest.process(data, processContext);
            }
            case 0x0108 -> {
                packetBody = processor_TerminalUpgradeResResponse.process(data, processContext);
            }
            case 0x0200 -> {
                packetBody = Position.read(data, packet.header.msgLen);
            }
            case 0x0201 -> {
                packetBody = QueryPositionResponse.read(data, packet.header.msgLen);
            }
            case 0x8202 -> {
                packetBody = processor_TempPositionFollow.process(data, processContext);
            }
            case 0x8203 -> {
                packetBody = processor_ConfirmAlarmMsg.process(data, processContext);
            }
            case 0x8300 -> {
                packetBody = TextInfoIssued.read(data, packet.header.msgLen);
            }
            case 0x8400 -> {
                packetBody = PhoneCallback.read(data, packet.header.msgLen);
            }
            case 0x8401 -> {
                packetBody = processor_SetPhoneText.process(data, processContext);
            }
            case 0x8500 -> {
                packetBody = VehicleControlRequest.read(data);
            }
            case 0x0500 -> {
                packetBody = VehicleControlResponse.read(data, packet.header.msgLen);
            }
            case 0x8600 -> {
                packetBody = SetCircleArea.read(data);
            }
            case 0x8601 -> {
                packetBody = processor_DeleteCircleArea.process(data, processContext);
            }
            case 0x8602 -> {
                packetBody = SetRectangleArea.read(data);
            }
            case 0x8603 -> {
                packetBody = processor_DeleteRectangleArea.process(data, processContext);
            }
            case 0x8604 -> {
                packetBody = SetPolygonArea.read(data);
            }
            case 0x8605 -> {
                packetBody = processor_DeletePolygonArea.process(data, processContext);
            }
            case 0x8606 -> {
                packetBody = SetPath.read(data);
            }
            case 0x8607 -> {
                packetBody = processor_DeletePath.process(data, processContext);
            }
            case 0x8608 -> {
                packetBody = processor_QueryAreaOrPathRequest.process(data, processContext);
            }
            case 0x0608 -> {
                packetBody = QueryAreaOrPathResponse.read(data);
            }
            case 0x8700 -> {
                packetBody = DrivingRecorderUpload.read(data, packet.header.msgLen);
            }
            case 0x8701 -> {
                packetBody = DrivingRecorderDownStream.read(data, packet.header.msgLen);
            }
            case 0x0701 -> {
                packetBody = processor_WaybillReport.process(data, processContext);
            }
            case 0x0702 -> {
                packetBody = DriverIdentityReport.read(data);
            }
            case 0x0704 -> {
                packetBody = PositionDataUpload.read(data);
            }
            case 0x0705 -> {
                packetBody = processor_CanDataUpload.process(data, processContext);
            }
            case 0x0800 -> {
                packetBody = processor_MultiMediaEventUpload.process(data, processContext);
            }
            case 0x0801 -> {
                packetBody = MultiMediaDataUploadRequest.read(data, packet.header.msgLen);
            }
            case 0x8800 -> {
                packetBody = MultiMediaDataUploadResponse.read(data, packet.header.msgLen);
            }
            case 0x8801 -> {
                packetBody = processor_CameraTakePhotoCmdRequest.process(data, processContext);
            }
            case 0x0805 -> {
                packetBody = processor_CameraTakePhotoCmdResponse.process(data, processContext);
            }
            case 0x8802 -> {
                packetBody = processor_StorageMultiMediaDataFetchRequest.process(data, processContext);
            }
            case 0x0802 -> {
                packetBody = StorageMultiMediaDataFetchResponse.read(data, packet.header.msgLen);
            }
            case 0x8803 -> {
                packetBody = processor_StorageMultiMediaDataUploadCmd.process(data, processContext);
            }
            case 0x8804 -> {
                packetBody = processor_RecordingStartCmd.process(data, processContext);
            }
            case 0x8805 -> {
                packetBody = processor_SingleMultiMediaDataFetchUploadCmd.process(data, processContext);
            }
            case 0x8900 -> {
                packetBody = DataDownStream.read(data, packet.header.msgLen);
            }
            case 0x0900 -> {
                packetBody = DataUpStream.read(data, packet.header.msgLen);
            }
            case 0x0901 -> {
                packetBody = DataCompressReport.read(data, packet.header.msgLen);
            }
            case 0x8A00 -> {
                packetBody = processor_PlatformRsa.process(data, processContext);
            }
            case 0x0A00 -> {
                packetBody = processor_TerminalRsa.process(data, processContext);
            }
            default -> throw ParseException.get("msgId[{}] not support", packet.header.msgId);
        }
        return packetBody;
    }

    @Override
    public void deProcess(ByteBuf data, ProcessContext<?> processContext, PacketBody instance) {
        Packet packet = (Packet) processContext.instance;
        switch (packet.header.msgId) {
            case 0x0001 -> {
                processor_TerminalCommonResponse.deProcess(data, processContext, (TerminalCommonResponse) instance);
            }
            case 0x8001 -> {
                processor_PlatformCommonResponse.deProcess(data, processContext, (PlatformCommonResponse) instance);
            }
            case 0x0002, 0x0004, 0x0003, 0x8104, 0x8107, 0x8201, 0x8204, 0x8702 -> {

            }
            case 0x8004 -> {
                processor_QueryServerTimeResponse.deProcess(data, processContext, (QueryServerTimeResponse) instance);
            }
            case 0x8003, 0x0005 -> {
                processor_ServerSubPacketRequest.deProcess(data, processContext, (ServerSubPacketRequest) instance);
            }
            case 0x0100 -> {
                ((TerminalRegisterRequest) instance).write(data);
            }
            case 0x8100 -> {
                ((TerminalRegisterResponse) instance).write(data);
            }
            case 0x0102 -> {
                processor_TerminalAuthentication.deProcess(data, processContext, (TerminalAuthentication) instance);
            }
            case 0x8103 -> {
                processor_SetTerminalParam.deProcess(data, processContext, (SetTerminalParam) instance);
            }
            case 0x8106 -> {
                processor_QueryTerminalParamRequest.deProcess(data, processContext, (QueryTerminalParamRequest) instance);
            }
            case 0x0104 -> {
                processor_QueryTerminalParamResponse.deProcess(data, processContext, (QueryTerminalParamResponse) instance);
            }
            case 0x8105 -> {
                ((TerminalControl) instance).write(data);
            }
            case 0x0107 -> {
                processor_QueryTerminalPropResponse.deProcess(data, processContext, (QueryTerminalPropResponse) instance);
            }
            case 0x8108 -> {
                processor_IssuedTerminalUpgradeRequest.deProcess(data, processContext, (IssuedTerminalUpgradeRequest) instance);
            }
            case 0x0108 -> {
                processor_TerminalUpgradeResResponse.deProcess(data, processContext, (TerminalUpgradeResResponse) instance);
            }
            case 0x0200 -> {
                ((Position) instance).write(data);
            }
            case 0x0201 -> {
                ((QueryPositionResponse) instance).write(data);
            }
            case 0x8202 -> {
                processor_TempPositionFollow.deProcess(data, processContext, (TempPositionFollow) instance);
            }
            case 0x8203 -> {
                processor_ConfirmAlarmMsg.deProcess(data, processContext, (ConfirmAlarmMsg) instance);
            }
            case 0x8300 -> {
                ((TextInfoIssued) instance).write(data);
            }
            case 0x8400 -> {
                ((PhoneCallback) instance).write(data);
            }
            case 0x8401 -> {
                processor_SetPhoneText.deProcess(data, processContext, (SetPhoneText) instance);
            }
            case 0x8500 -> {
                ((VehicleControlRequest) instance).write(data);
            }
            case 0x0500 -> {
                ((VehicleControlResponse) instance).write(data);
            }
            case 0x8600 -> {
                ((SetCircleArea) instance).write(data);
            }
            case 0x8601 -> {
                processor_DeleteCircleArea.deProcess(data, processContext, (DeleteCircleArea) instance);
            }
            case 0x8602 -> {
                ((SetRectangleArea) instance).write(data);
            }
            case 0x8603 -> {
                processor_DeleteRectangleArea.deProcess(data, processContext, (DeleteRectangleArea) instance);
            }
            case 0x8604 -> {
                ((SetPolygonArea) instance).write(data);
            }
            case 0x8605 -> {
                processor_DeletePolygonArea.deProcess(data, processContext, (DeletePolygonArea) instance);
            }
            case 0x8606 -> {
                ((SetPath) instance).write(data);
            }
            case 0x8607 -> {
                processor_DeletePath.deProcess(data, processContext, (DeletePath) instance);
            }
            case 0x8608 -> {
                processor_QueryAreaOrPathRequest.deProcess(data, processContext, (QueryAreaOrPathRequest) instance);
            }
            case 0x0608 -> {
                ((QueryAreaOrPathResponse) instance).write(data);
            }
            case 0x8700 -> {
                ((DrivingRecorderUpload) instance).write(data);
            }
            case 0x8701 -> {
                ((DrivingRecorderDownStream) instance).write(data);
            }
            case 0x0701 -> {
                processor_WaybillReport.deProcess(data, processContext, (WaybillReport) instance);
            }
            case 0x0702 -> {
                ((DriverIdentityReport) instance).write(data);
            }
            case 0x0704 -> {
                ((PositionDataUpload) instance).write(data);
            }
            case 0x0705 -> {
                processor_CanDataUpload.deProcess(data, processContext, (CanDataUpload) instance);
            }
            case 0x0800 -> {
                processor_MultiMediaEventUpload.deProcess(data, processContext, (MultiMediaEventUpload) instance);
            }
            case 0x0801 -> {
                ((MultiMediaDataUploadRequest) instance).write(data);
            }
            case 0x8800 -> {
                ((MultiMediaDataUploadResponse) instance).write(data);
            }
            case 0x8801 -> {
                processor_CameraTakePhotoCmdRequest.deProcess(data, processContext, (CameraTakePhotoCmdRequest) instance);
            }
            case 0x0805 -> {
                processor_CameraTakePhotoCmdResponse.deProcess(data, processContext, (CameraTakePhotoCmdResponse) instance);
            }
            case 0x8802 -> {
                processor_StorageMultiMediaDataFetchRequest.deProcess(data, processContext, (StorageMultiMediaDataFetchRequest) instance);
            }
            case 0x0802 -> {
                ((StorageMultiMediaDataFetchResponse) instance).write(data);
            }
            case 0x8803 -> {
                processor_StorageMultiMediaDataUploadCmd.deProcess(data, processContext, (StorageMultiMediaDataUploadCmd) instance);
            }
            case 0x8804 -> {
                processor_RecordingStartCmd.deProcess(data, processContext, (RecordingStartCmd) instance);
            }
            case 0x8805 -> {
                processor_SingleMultiMediaDataFetchUploadCmd.deProcess(data, processContext, (SingleMultiMediaDataFetchUploadCmd) instance);
            }
            case 0x8900 -> {
                ((DataDownStream) instance).write(data);
            }
            case 0x0900 -> {
                ((DataUpStream) instance).write(data);
            }
            case 0x0901 -> {
                ((DataCompressReport) instance).write(data);
            }
            case 0x8A00 -> {
                processor_PlatformRsa.process(data, processContext);
            }
            case 0x0A00 -> {
                processor_TerminalRsa.process(data, processContext);
            }
            default -> throw ParseException.get("msgId[{}] not support", packet.header.msgId);
        }
    }

}
