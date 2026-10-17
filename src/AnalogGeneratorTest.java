
import java.util.Arrays;

public class AnalogGeneratorTest {

    public static void main(String[] args) {

        double[] samples =
                AnalogSignalGenerator.generateSineWave(
                        20,      // Number of samples
                        1.0,     // Amplitude
                        5.0,     // Frequency in Hz
                        100.0    // Sampling rate in samples/second
                );

        System.out.println("Generated analog samples:");
        System.out.println(Arrays.toString(samples));

        System.out.println("\nTotal samples: " + samples.length);
    }
}
