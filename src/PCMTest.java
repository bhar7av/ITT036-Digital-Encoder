
import java.util.Arrays;

public class PCMTest {

    public static void main(String[] args) {

        double[] samples =
                AnalogSignalGenerator.generateSineWave(
                        20,
                        1.0,
                        5.0,
                        100.0
                );

        // 3 bits per sample, covering the range -1.0 to +1.0.
        PCM pcm = new PCM(3, -1.0, 1.0);

        String[] encoded = pcm.encode(samples);
        double[] reconstructed = pcm.decode(encoded);

        System.out.println("Original samples:");
        System.out.println(Arrays.toString(samples));

        System.out.println("\nPCM codewords:");
        System.out.println(Arrays.toString(encoded));

        System.out.println("\nCombined PCM bitstream:");
        System.out.println(pcm.toBitstream(encoded));

        System.out.println("\nReconstructed samples:");
        System.out.println(Arrays.toString(reconstructed));

        System.out.println("\nBits per sample: "
                + pcm.getBitsPerSample());

        System.out.println("Quantization levels: "
                + pcm.getLevels());

        System.out.println("Step size: "
                + pcm.getStepSize());

        double totalSquaredError = 0.0;

        for (int i = 0; i < samples.length; i++) {
            double error = samples[i] - reconstructed[i];
            totalSquaredError += error * error;
        }

        double mse = totalSquaredError / samples.length;

        System.out.println("Mean squared quantization error: " + mse);
    }
}
