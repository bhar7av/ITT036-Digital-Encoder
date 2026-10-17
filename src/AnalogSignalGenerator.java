
import java.util.Scanner;

public class AnalogSignalGenerator {

    private static final Scanner scanner = new Scanner(System.in);

    // Generate a sine wave from sampled analog values.
    public static double[] generateSineWave(
            int numberOfSamples,
            double amplitude,
            double frequency,
            double samplingRate) {

        validateParameters(
                numberOfSamples, amplitude, frequency, samplingRate
        );

        double[] samples = new double[numberOfSamples];

        for (int i = 0; i < numberOfSamples; i++) {

            double time = (double) i / samplingRate;

            samples[i] = amplitude
                    * Math.sin(2.0 * Math.PI * frequency * time);
        }

        return samples;
    }

    // Generate a cosine wave from sampled analog values.
    public static double[] generateCosineWave(
            int numberOfSamples,
            double amplitude,
            double frequency,
            double samplingRate) {

        validateParameters(
                numberOfSamples, amplitude, frequency, samplingRate
        );

        double[] samples = new double[numberOfSamples];

        for (int i = 0; i < numberOfSamples; i++) {

            double time = (double) i / samplingRate;

            samples[i] = amplitude
                    * Math.cos(2.0 * Math.PI * frequency * time);
        }

        return samples;
    }

    // Allow the user to enter custom sample values.
    public static double[] enterCustomSamples() {

        System.out.print("Enter the number of samples (1-10000): ");
        int count = readInt();

        if (count < 1 || count > 10000) {
            throw new IllegalArgumentException(
                    "Sample count must be between 1 and 10000."
            );
        }

        double[] samples = new double[count];

        System.out.println("Enter " + count + " sample values:");

        for (int i = 0; i < count; i++) {
            System.out.print("Sample " + (i + 1) + ": ");

            if (!scanner.hasNextDouble()) {
                scanner.nextLine();
                throw new IllegalArgumentException(
                        "Please enter a valid numeric sample."
                );
            }

            samples[i] = scanner.nextDouble();
        }

        scanner.nextLine();
        return samples;
    }

    private static void validateParameters(
            int numberOfSamples,
            double amplitude,
            double frequency,
            double samplingRate) {

        if (numberOfSamples < 1 || numberOfSamples > 10000) {
            throw new IllegalArgumentException(
                    "Sample count must be between 1 and 10000."
            );
        }

        if (!Double.isFinite(amplitude) || amplitude <= 0) {
            throw new IllegalArgumentException(
                    "Amplitude must be a finite positive number."
            );
        }

        if (!Double.isFinite(frequency) || frequency <= 0) {
            throw new IllegalArgumentException(
                    "Frequency must be a finite positive number."
            );
        }

        if (!Double.isFinite(samplingRate) || samplingRate <= 0) {
            throw new IllegalArgumentException(
                    "Sampling rate must be a finite positive number."
            );
        }

        if (frequency >= samplingRate / 2.0) {
            throw new IllegalArgumentException(
                    "Frequency must be below half the sampling rate."
            );
        }
    }

    private static int readInt() {
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            throw new IllegalArgumentException(
                    "Please enter a valid integer."
            );
        }

        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}
