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

        int[] func = KmpMatcher.func(template);

        LOGGER.info(Arrays.toString(func));

        assertEquals(0, func[0]);
        assertEquals(1, func[1]);
        assertEquals(0, func[2]);
        assertEquals(1, func[3]);
        assertEquals(2, func[4]);
        assertEquals(3, func[5]);

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
