package kmp.matcher;

import java.util.ArrayList;
import java.util.List;

public class KmpMatcher {
    public static int[] prefixFunc(String template) {
        int[] prefixFunc = new int[template.length()];
        prefixFunc[0] = 0;

        for (int i = 1; i < template.length(); i++) {
            for (int j = 0; j < template.length()-i; j++) {
                if (template.charAt(j) == template.charAt(i+j)) {
                    prefixFunc[i+j] = Math.max(prefixFunc[i+j], j+1);
                }
                else {
                    prefixFunc[i+j] = Math.max(prefixFunc[i+j], 0);
                    break;
                }
            }
        }
        return prefixFunc;
    }

    public static List<Integer> kmpSearch(String s, String template) {
        List<Integer> found = new ArrayList<>();

        int[] prefix = prefixFunc(template);

        int i = 0, j = 0;

        while (i < s.length()) {
            if (s.charAt(i) == template.charAt(j)) {
                i++;
                j++;
            }
            if (j == template.length()) {
                found.add(i - j);
                j = prefix[j-1];
            } else if (i < s.length() && s.charAt(i) != template.charAt(j)) {
                if (j != 0) {
                    j = prefix[j-1];
                } else i++;
            }
        }

        return found;
    }
}
