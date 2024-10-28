package grpc.java;

import grpc.java.sample.Generator;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import vez.grpc.CreateLaptopRequest;
import vez.grpc.CreateLaptopResponse;
import vez.grpc.Laptop;
import vez.grpc.LaptopServiceGrpc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaptopClient {

    private static Logger logger = Logger.getLogger(LaptopClient.class.getName());

    private final ManagedChannel channel;
    private final LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub;

    public LaptopClient(String host, int port) {
        logger.info("Creating LaptopClient with host: " + host + " and port: " + port);
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void createLaptop(Laptop laptop) {
        logger.info("Creating a laptop");
        CreateLaptopRequest request = CreateLaptopRequest.newBuilder().setLaptop(laptop).build();
        CreateLaptopResponse response;
        try {
            response = blockingStub.createLaptop(request);
            logger.info("Laptop created successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating laptop: " + e.getMessage());
            return;
        }

        logger.info("Laptop ID: " + response.getId());
    }

    public static void main(String[] args) throws InterruptedException {
        logger.info("Hello from LaptopClient");

        LaptopClient client = new LaptopClient("localhost", 8080);
        Laptop laptop = new Generator().NewLaptop().toBuilder().setId("").build();

        try {
            client.createLaptop(laptop);
        } finally {
            client.shutdown();
        }
    }
}
