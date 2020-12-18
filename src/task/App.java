package task;

import task.service.Response;

public class App {

    public static void main(String[] args) {
        String s0 = "15";
        String s1 = "C 1.1 8.15.1 P 15.10.2012 83";
        String s2 = "C 1 10.1 P 01.12.2012 65";
        String s3 = "C 1.1 5.5.1 P 01.11.2012 117";
        String s4 = "D 1.1 8 P 01.01.2012-01.12.2012";
        String s5 = "C 3 10.2 N 02.10.2012 100";
        String s6 = "D 1 * P 08.10.2012-20.11.2012";
        String s7 = "D 3 10 P 01.12.2012";
        String s8 = "C 10.2 8.15.2 N 14.10.2014 48";
        String s9 = "D 10 8.15 N 10.10.2014";
        String s10 = "C * 9.2.1 N 15.12.2012 80";
        String s11 = "C 9 9 N 10.10.2012 22";
        String s12 = "D 9 9 N 01.10.2012-01.01.2013";
        String s13 = "C 1.1 * P 10.10.2012 60";
        String s14 = "D 1 * P 02.10.2012";
        String s15 = "D 1 5.5 P 30.10.2012-02.11.2012";
        String[] input = new String[]{s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15};


        Response response = new Response();
        response.setInput(input);
        for (String res: response.getResponse()) {
            System.out.println(res);
        }
    }
}
