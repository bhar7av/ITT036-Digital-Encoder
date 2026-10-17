
public class PCMPlotterTest {

    public static void main(String[] args) {

        double[] original =
                AnalogSignalGenerator.generateSineWave(
                        100,
                        1.0,
                        5.0,
                        100.0
                );

        PCM pcm = new PCM(3, -1.0, 1.0);

        String[] encoded = pcm.encode(original);
        double[] reconstructed = pcm.decode(encoded);

        PCMPlotter.plot(original, reconstructed);
    }
}
