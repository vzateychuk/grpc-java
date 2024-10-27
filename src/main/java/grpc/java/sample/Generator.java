package grpc.java.sample;

import vez.grpc.*;

import java.util.Random;

public class Generator {

    private final Random rnd;

    public Generator() {
        rnd = new Random();
    }

    public Keyboard NewKeyboard() {
        return Keyboard.newBuilder()
                .setLayout(randomLayout())
                .setBacklit(rnd.nextBoolean())
                .build();
    }

    public Cpu NewCpu() {
        String brand = randomBrand();
        String name = randomCpuName();
        return Cpu.newBuilder()
                .setCores(rnd.nextInt(8) + 1)
                .setBrand(brand)
                .setName(name)
                .build();
    }

    public Memory NewMemory() {
        return Memory.newBuilder()
                .setValue(rnd.nextInt(64) + 1)
                .setUnit(Memory.Unit.GB)
                .build();
    }

    public Storage NewHDD() {
        return Storage.newBuilder()
                .setDriver(Storage.Driver.HDD)
                .setMemory(NewMemory())
                .build();
    }

    public Storage NewSSD() {
        return Storage.newBuilder()
                .setDriver(Storage.Driver.SSD)
                .setMemory(NewMemory())
                .build();
    }

    public Screen NewScreen() {
        int height = rnd.nextInt(1080) + 720;
        int width = rnd.nextInt(1920) + 1280;
        Screen.Resolution resolution = Screen.Resolution.newBuilder()
                .setHeight(height)
                .setWidth(width)
                .build();

        Screen.Panel panel = randomScreenPanel();

        return Screen.newBuilder()
                .setResolution(resolution)
                .setPanel(panel)
                .setSize(rnd.nextInt(15) + 10)
                .build();
    }

    public Laptop NewLaptop() {
        return Laptop.newBuilder()
                .setBrand(randomBrand())
                .setName(randomLaptopName())
                .setKeyboard(NewKeyboard())
                .setCpu(NewCpu())
                .setRam(NewMemory())
                .setScreen(NewScreen())
                .addStorages(NewHDD())
                .addStorages(NewSSD())
                .build();
    }

    // Test
    public static void main(String[] args) {
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptop();
        System.out.println(laptop);
    }

    //region PRIVATE METHODS
    private String randomLaptopName() {
        return randomStringFromSet("MacBook", "ThinkPad", "Surface", "ZenBook", "XPS", "Spectre", "EliteBook");
    }

    private Screen.Panel randomScreenPanel() {
        if (rnd.nextInt(2) == 1) {
            return Screen.Panel.IPS;
        }
        return Screen.Panel.OLED;
    }

    private String randomCpuName() {
        return randomStringFromSet("i3", "i5", "i7", "i9", "Ryzen 3", "Ryzen 5", "Ryzen 7", "Ryzen 9");
    }

    private String randomBrand() {
        return randomStringFromSet("Intel", "AMD", "Apple", "Qualcomm");
    }

    private String randomStringFromSet(String ...strings) {
        if (strings.length == 0) {
            return "";
        }
        return strings[rnd.nextInt(strings.length)];
    }

    private Keyboard.Layout randomLayout() {
        int val = rnd.nextInt(2);
        if (val == 1) {
            return Keyboard.Layout.DVORAK;
        }
        return Keyboard.Layout.QWERTY;
    }
    //endregion PRIVATE METHODS
}
