package grpc.java.service;

import grpc.java.exception.AlreadyExistsException;
import grpc.java.store.LaptopStore;
import io.grpc.Status;
import vez.grpc.CreateLaptopRequest;
import vez.grpc.CreateLaptopResponse;
import vez.grpc.Laptop;
import vez.grpc.LaptopServiceGrpc;

import java.util.UUID;
import java.util.logging.Logger;

public class LaptopService extends LaptopServiceGrpc.LaptopServiceImplBase {

    private static final Logger logger = Logger.getLogger(LaptopService.class.getName());

    private final LaptopStore store;

    public LaptopService(LaptopStore store) {
        this.store = store;
    }

    @Override
    public void createLaptop(CreateLaptopRequest req, io.grpc.stub.StreamObserver<vez.grpc.CreateLaptopResponse> respObserver) {
        logger.info("Received a create-laptop request");
        Laptop laptop = req.getLaptop();

        String id = laptop.getId();
        logger.info("Create laptop with ID: " + id);

        UUID uuid;
        if (id.isEmpty()) {
            logger.warning("ID is empty, generatring a new ID...");
            uuid = UUID.randomUUID();
        }

        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            respObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid UUID: " + e.getMessage())
                            .asRuntimeException()
            );
            return;
        }

        Laptop other = Laptop.newBuilder().setId(uuid.toString()).build();

        try {
            store.Save(other);
        } catch (AlreadyExistsException e) {
            respObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription("Laptop with ID already exists: " + e.getMessage())
                            .asRuntimeException()
            );
            return;
        } catch (Exception e) {
            respObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal error: " + e.getMessage())
                            .asRuntimeException()
            );
            return;
        }

        CreateLaptopResponse res = CreateLaptopResponse.newBuilder()
                .setId(other.getId())
                .build();

        respObserver.onNext(res);
        respObserver.onCompleted();

        logger.info("Laptop saved with ID: " + other.getId());
    }
}
