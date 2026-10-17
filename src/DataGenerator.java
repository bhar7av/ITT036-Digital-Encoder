
import java.util.Random;

public class DataGenerator {

    private static final Random RANDOM = new Random();

    public static String generateRandomBits(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException(
                    "Length must be positive."
            );
        }

        StringBuilder bits = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            bits.append(RANDOM.nextInt(2));
        }

        return bits.toString();
    }

    public static String generateWithZeroRun(
            int length, int zeroCount) {

        if (length < zeroCount || zeroCount <= 0) {
            throw new IllegalArgumentException(
                    "Length must be at least " + zeroCount
            );
        }

        StringBuilder bits = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            bits.append(RANDOM.nextInt(2));
        }

        int start = RANDOM.nextInt(length - zeroCount + 1);

        for (int i = start; i < start + zeroCount; i++) {
            bits.setCharAt(i, '0');
        }

        return bits.toString();
    }
}
