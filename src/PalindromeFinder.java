
public class PalindromeFinder {

    public static String findLongestPalindrome(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        // Transform the string to handle odd and even palindromes.
        StringBuilder transformed = new StringBuilder("^#");

        for (int i = 0; i < s.length(); i++) {
            transformed.append(s.charAt(i)).append('#');
        }

        transformed.append('$');

        String t = transformed.toString();
        int n = t.length();

        int[] radius = new int[n];
        int center = 0;
        int right = 0;

        int bestCenter = 0;
        int bestLength = 0;

        for (int i = 1; i < n - 1; i++) {

            int mirror = 2 * center - i;

            if (i < right) {
                radius[i] = Math.min(
                        right - i, radius[mirror]
                );
            }

            while (t.charAt(i + 1 + radius[i])
                    == t.charAt(i - 1 - radius[i])) {
                radius[i]++;
            }

            if (i + radius[i] > right) {
                center = i;
                right = i + radius[i];
            }

            if (radius[i] > bestLength) {
                bestLength = radius[i];
                bestCenter = i;
            }
        }

        int start = (bestCenter - bestLength) / 2;

        return s.substring(start, start + bestLength);
    }
}
