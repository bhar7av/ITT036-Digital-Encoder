
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final Map<Integer, Supplier<LineEncoder>> ENCODERS =
            new LinkedHashMap<>();

    private static final Map<Integer, Supplier<LineDecoder>> DECODERS =
            new LinkedHashMap<>();

    static {
        ENCODERS.put(1, NRZL::new);
        ENCODERS.put(2, NRZI::new);
        ENCODERS.put(3, Manchester::new);
        ENCODERS.put(4, DifferentialManchester::new);
        ENCODERS.put(5, AMI::new);
        ENCODERS.put(6, B8ZS::new);
        ENCODERS.put(7, HDB3::new);

        DECODERS.put(1, NRZLDecoder::new);
        DECODERS.put(2, NRZIDecoder::new);
        DECODERS.put(3, ManchesterDecoder::new);
        DECODERS.put(4, DifferentialManchesterDecoder::new);
        DECODERS.put(5, AMIDecoder::new);
        DECODERS.put(6, B8ZSDecoder::new);
        DECODERS.put(7, HDB3Decoder::new);
    }

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println(" DIGITAL DATA AND SIGNAL CONVERSION ");
        System.out.println("========================================");

        try {
            System.out.println("\n1. Digital data and line coding");
            System.out.println("2. Analog signal and PCM");
            System.out.print("Choose a mode: ");

            int mode = readInt();

            switch (mode) {
                case 1:
                    runDigitalMode();
                    break;

                case 2:
                    runPCMMode();
                    break;

                default:
                    System.out.println("Invalid mode.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void runDigitalMode() {

        String data = generateData();

        System.out.println("\nOriginal data: " + data);

        String palindrome =
                PalindromeFinder.findLongestPalindrome(data);

        System.out.println("Longest palindrome: " + palindrome);
        System.out.println("Palindrome length: " + palindrome.length());

        System.out.println("\n--- LINE CODING ---");
        System.out.println("1. NRZ-L");
        System.out.println("2. NRZ-I");
        System.out.println("3. Manchester");
        System.out.println("4. Differential Manchester");
        System.out.println("5. AMI");
        System.out.println("6. B8ZS");
        System.out.println("7. HDB3");
        System.out.print("Choose a scheme: ");

        int choice = readInt();

        Supplier<LineEncoder> factory = ENCODERS.get(choice);

        if (factory == null) {
            throw new IllegalArgumentException("Invalid line-coding choice.");
        }

        LineEncoder encoder = factory.get();
        double[] signal = encoder.encode(data);

        System.out.println("\nScheme: " + encoder.getName());
        System.out.println("Encoded signal: " + Arrays.toString(signal));

        SignalPlotter.plot(encoder.getName(), data, signal);

        System.out.println("\n1. Decode signal");
        System.out.println("2. Skip decoding");
        System.out.print("Choose: ");

        int decodeChoice = readInt();

        if (decodeChoice == 1) {
            LineDecoder decoder = DECODERS.get(choice).get();
            String decoded = decoder.decode(signal);

            System.out.println("Decoded data: " + decoded);
            System.out.println(
                    "Verification: "
                            + (data.equals(decoded) ? "PASS" : "FAIL")
            );
        }
    }

    private static void runPCMMode() {

        System.out.println("\n--- ANALOG SIGNAL GENERATION ---");
        System.out.println("1. Generate sine wave");
        System.out.println("2. Generate cosine wave");
        System.out.println("3. Enter custom samples");
        System.out.print("Choose an option: ");

        int choice = readInt();
        double[] samples;

        if (choice == 1 || choice == 2) {

            System.out.print("Number of samples (1-10000): ");
            int count = readInt();

            System.out.print("Amplitude: ");
            double amplitude = readDouble();

            System.out.print("Frequency in Hz: ");
            double frequency = readDouble();

            System.out.print("Sampling rate in samples/second: ");
            double samplingRate = readDouble();

            if (choice == 1) {
                samples = AnalogSignalGenerator.generateSineWave(
                        count, amplitude, frequency, samplingRate
                );
            } else {
                samples = AnalogSignalGenerator.generateCosineWave(
                        count, amplitude, frequency, samplingRate
                );
            }

        } else if (choice == 3) {

            System.out.print("Number of samples (1-10000): ");
            int count = readInt();

            if (count < 1 || count > 10000) {
                throw new IllegalArgumentException(
                        "Sample count must be between 1 and 10000."
                );
            }

            samples = new double[count];

            for (int i = 0; i < count; i++) {
                System.out.print("Enter sample " + (i + 1) + ": ");
                samples[i] = readDouble();
            }

        } else {
            throw new IllegalArgumentException(
                    "Invalid analog signal option."
            );
        }

        System.out.println("\nOriginal analog samples:");
        System.out.println(Arrays.toString(samples));

        System.out.print("\nBits per sample (1-16): ");
        int bits = readInt();

        System.out.print("Minimum quantization value: ");
        double minValue = readDouble();

        System.out.print("Maximum quantization value: ");
        double maxValue = readDouble();

        PCM pcm = new PCM(bits, minValue, maxValue);

        String[] encoded = pcm.encode(samples);
        double[] reconstructed = pcm.decode(encoded);

        System.out.println("\nPCM codewords:");
        System.out.println(Arrays.toString(encoded));

        System.out.println("\nPCM bitstream:");
        System.out.println(pcm.toBitstream(encoded));

        System.out.println("\nReconstructed samples:");
        System.out.println(Arrays.toString(reconstructed));

        double squaredError = 0.0;

        for (int i = 0; i < samples.length; i++) {
            double error = samples[i] - reconstructed[i];
            squaredError += error * error;
        }

        System.out.println(
                "\nMean squared error: "
                        + squaredError / samples.length
        );

        System.out.println("Quantization levels: " + pcm.getLevels());
        System.out.println("Step size: " + pcm.getStepSize());

        PCMPlotter.plot(samples, reconstructed);
    }

    private static String generateData() {

        System.out.println("\n--- DATA GENERATION ---");
        System.out.println("1. Random digital data");
        System.out.println("2. Data containing 4 consecutive zeros");
        System.out.println("3. Data containing 8 consecutive zeros");
        System.out.println("4. Enter binary data manually");
        System.out.print("Choose: ");

        int choice = readInt();

        switch (choice) {
            case 1:
            case 2:
            case 3:
                System.out.print("Enter data length: ");
                int length = readInt();

                if (length <= 0 || length > 10000) {
                    throw new IllegalArgumentException(
                            "Length must be between 1 and 10000."
                    );
                }

                if (choice == 1) {
                    return DataGenerator.generateRandomBits(length);
                }

                int zeroCount = choice == 2 ? 4 : 8;

                if (length < zeroCount) {
                    throw new IllegalArgumentException(
                            "Length must be at least " + zeroCount + "."
                    );
                }

                return DataGenerator.generateWithZeroRun(
                        length, zeroCount
                );

            case 4:
                System.out.print("Enter binary data: ");
                String data = scanner.nextLine().trim();

                if (!data.matches("[01]+")) {
                    throw new IllegalArgumentException(
                            "Enter only 0s and 1s."
                    );
                }

                return data;

            default:
                throw new IllegalArgumentException(
                        "Invalid data-generation choice."
                );
        }
    }

    private static int readInt() {
        String value = scanner.nextLine().trim();

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a valid integer.");
        }
    }

    private static double readDouble() {
        String value = scanner.nextLine().trim();

        try {
            double number = Double.parseDouble(value);

            if (!Double.isFinite(number)) {
                throw new NumberFormatException();
            }

            return number;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Enter a valid finite number."
            );
        }
    }
}
