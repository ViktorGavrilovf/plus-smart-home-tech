package ru.practicum.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.mapper.ProtoToAvroHubMapper;
import ru.practicum.mapper.ProtoToAvroSensorMapper;
import ru.practicum.service.ProducerService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CollectorGrpcController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final ProducerService producerService;
    private final ProtoToAvroSensorMapper sensorMapper;
    private final ProtoToAvroHubMapper hubMapper;

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("gRPC: получен SensorEventProto: {}", request);
        try {
            var avro = sensorMapper.toAvro(request);
            producerService.sendSensorEvent(avro);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError(responseObserver, e, "collectSensor");
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("gRPC: получен HubEventProto: {}", request);
        try {
            var avro = hubMapper.toAvro(request);
            producerService.sendHubEvent(avro);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            handleError(responseObserver, e, "collectHub");
        }
    }

    private void handleError(StreamObserver<?> responseObserver, Exception e, String context) {
        log.error("Ошибка в {}: {}", context, e.getMessage(), e);
        responseObserver.onError(new StatusRuntimeException(
                Status.INTERNAL.withDescription(e.getLocalizedMessage()).withCause(e)
        ));
    }
}
