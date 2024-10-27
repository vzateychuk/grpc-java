package grpc.java;

import grpc.java.service.LaptopService;
import grpc.java.store.LaptopInMemStore;
import grpc.java.store.LaptopStore;
import io.grpc.ServerBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class LaptopServer {

    private static Logger logger = Logger.getLogger(LaptopServer.class.getName());

    private final int port;
    private final io.grpc.Server server;

    public LaptopServer(int port, LaptopStore store) {
        this(ServerBuilder.forPort(port), port, store);
    }

    public LaptopServer(ServerBuilder builder, int port, LaptopStore store) {
        this.port = port;
        LaptopService service = new LaptopService(store);
        server = builder.addService(service).build();
    }

    public void start() throws Exception {
        server.start();

        logger.info("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                LaptopServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(20, TimeUnit.SECONDS);
        }

    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        LaptopStore store = new LaptopInMemStore();
        LaptopServer server = new LaptopServer(8080, store);
        server.start();
        server.blockUntilShutdown();
    }
}
