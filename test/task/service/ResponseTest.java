package task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {
    private Response response;

    @BeforeEach
    public void initData(){
        response = new Response();
    }

    @ParameterizedTest
    @MethodSource("task.service.InputProvider#inputArray")
    public void getResponseTest1(String[] input, String[] expected){
        response = new Response();
        response.setInput(input);
        String[] actual = response.getResponse();
        System.out.print("Actual: " +Arrays.toString(actual));
        System.out.print("Expected: " +Arrays.toString(expected) +"\n");

        assertEquals(expected.length, actual.length);
        for (int i=0; i<expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
