package task.service;

import java.util.Arrays;
import java.util.Collection;

public class InputProvider {
    // Test1
    private static String[] input1 = new String[]{"4", "C 1.1 8.15.1 P 15.10.2012 83",
            "C 1 10.1 P 01.12.2012 65", "C 1.1 5.5.1 P 01.11.2012 117", "D 1.1 8 P 01.01.2012-01.12.2012"};
    private static String[] expected1 = new String[]{"83"};

    // Test2
    private static String[] input2 = new String[]{"3", "C 1.1 8.15.1 P 15.10.2012 83",
            "C 1 10.1 P 01.12.2012 65", "D 1 10 P 01.01.2012-02.12.2012"};
    private static String[] expected2 = new String[]{"65"};

    // Test3
    private static String[] input3 = new String[]{"0"};
    private static String[] expected3 = new String[]{"-"};

    // Test4
    private static String[] input4 = new String[]{"1", "D 1 10 P 01.01.2012-02.12.2012"};
    private static String[] expected4 = new String[]{"-"};

    // Test5
    private static String[] input5 = new String[]{"2", "C 10.1 10.20.5 P 01.01.2012 86", "D * * P 01.01.2012"};
    private static String[] expected5 = new String[]{"86"};

    // Test6
    private static String[] input6 = new String[]{"3", "C 10.1 10.20.5 P 01.01.2012 53",
            "C 10.2 10.20 P 01.01.2012 20", "D 10 10.20 P 01.01.2012"};
    private static String[] expected6 = new String[]{"36"};

    // Test7
    private static String[] input7 = new String[]{"3", "C * 8.2 P 01.01.2012 40",
            "C 1.1 * P 01.01.2012 30", "D 1.1 8 P 01.01.2012"};
    private static String[] expected7 = new String[]{"35"};

    // Test8
    private static String[] input8 = new String[]{"3", "C * 8.2 P 01.01.2012 40",
            "C 1.1 * P 01.01.2012 30", "D 1.1 8 P 01.01.2012"};
    private static String[] expected8 = new String[]{"35"};

    // Test9
    private static String[] input9 = new String[]{"3", "C * 8.2 P 01.01.2012 100",
            "C 1.1 * N 01.01.2012 30", "D 1.1 8 P 01.01.2012"};
    private static String[] expected9 = new String[]{"100"};

    // Test10
    private static String[] input10 = new String[]{"6", "C * 8.2 P 01.01.2012 100",
            "C 1.4 * P 01.01.2012 30", "C 1.1 8.12 P 01.02.2012 50", "D 1.1 8 P 01.01.2012-02.02.2012",
            "C 2.3 8.10 N 10.10.2012 90", "D 2 8.10 N 01.10.2012-01.11.2012", "C * * N 11.10.2012 100",
            "D 3 8.10 P 01.10.2012-01.11.2012"
    };
    private static String[] expected10 = new String[]{"75", "90", "-"};

    // Test11
    private static String[] input11 = new String[]{"9", "C 1.1 8.15.1 P 15.10.2012 83",
            "C 1 10.1 P 01.12.2012 65", "C 1.1 5.5.1 P 01.11.2012 117", "D 1.1 8 P 01.01.2012-01.12.2012",
            "C 3 10.2 N 02.10.2012 100", "D 1 * P 08.10.2012-20.11.2012", "D 3 10 P 01.12.2012",
            "C 10.2 8.15.2 N 14.10.2014 48", "D 10 8.15 N 10.10.2014"
    };
    private static String[] expected11 = new String[]{"83", "100", "-", "48"};

    private static Object[] test= new Object[][]{{input1,expected1}, {input2,expected2}, {input3, expected3},
            {input4,expected4}, {input5,expected5}, {input6,expected6}, {input7,expected7}, {input8,expected8},
            {input9,expected9}, {input10,expected10}, {input11,expected11}
    };

    public static Collection<Object> inputArray(){
        return Arrays.asList(test);
    }
}
