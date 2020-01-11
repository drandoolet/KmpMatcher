package kmp;

import kmp.matcher.KmpMatcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class KmpMatcherTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(KmpMatcherTest.class);

    @Test
    public void testPrefixFunc() {
        String template = "AABAAB";
        int[] expected = new int[] {0, 1, 0, 1, 2, 3};

        int[] func = KmpMatcher.prefixFunc(template);

        LOGGER.info(Arrays.toString(func));

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], func[i]);
        }
    }

    @Test
    public void testKmpMatch() {
        String text = "AABAABAAAAAABAABAABAABBAAAB";
        String template = "AABAAB";

        List<Integer> found = KmpMatcher.kmpSearch(text, template);

        LOGGER.info(found.toString());

        assertEquals(List.of(0, 10, 13, 16), found);
        assertEquals(Arrays.asList(0, 10, 13, 16), found);
    }

    @Test
    public void testKmpCustom() {
        String text = "MAMAMYLARAMUMAMAMAN";
        String template = "MAMA";
        List<Integer> expected = List.of(0, 12, 14);

        List<Integer> found = KmpMatcher.kmpSearch(text, template);

        LOGGER.info(found.toString());

        assertEquals(expected, found);
    }

    @Test
    public void testPrefixFuncEffectiveness() {
        final int trials = 10000;
        final int preheat = 10000;
        final int log_frequency = trials / 20;
        final List<Long> timeList = new ArrayList<>((int) (trials * 1.5));

        LOGGER.info("Preheating started...");
        long phStart = System.nanoTime();
        for (int i = 0; i < preheat; i++) {
            String s = RandomStringer.randomStringOfCapitalLetters(15);
            String template = RandomStringer.randomStringOfCapitalLetters(5);

            KmpMatcher.kmpSearch(s, template);
        }
        LOGGER.info(String.format("Preheating complete in %d ns.", (System.nanoTime() - phStart)));

        for (int i = 0; i < trials; i++) {
            String s = RandomStringer.randomStringOfCapitalLetters(15);
            String template = RandomStringer.randomStringOfCapitalLetters(5);

            long start = System.nanoTime();
            KmpMatcher.kmpSearch(s, template);
            long end = System.nanoTime();
            long time = end - start;

            timeList.add(time);
            if (i % log_frequency == 0) System.out.println(String.format("Trial #%d has taken %d ns", i, time));
        }

        long avgTime = Double
                .valueOf(timeList
                        .stream()
                        .mapToLong(Long::longValue)
                        .average()
                        .getAsDouble())
                .longValue();

        LOGGER.info(String.format("\n\nAverage completion time: %d ns", avgTime));

        Assert.assertTrue(avgTime < 2000);
    }

    private static class RandomStringer {
        private static final Random random = new Random();

        private static String randomStringOfCapitalLetters(int length) {
            char[] chars = new char[length];

            for (int i = 0; i < length; i++) {
                chars[i] = (char) (65 + random.nextInt(26));
            }

            return String.valueOf(chars);
        }
    }
}
