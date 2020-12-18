package task.service;

import task.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Response {
    private static final String serviceRegex = "(\\*|([1-9]|10)|(([1-9]|10)\\.[1-3]))";
    private static final String questionRegex = "(\\*|([1-9]|10)|(([1-9]|10)\\.([1-9]|1[0-9]|20))|(([1-9]|10)\\.([1-9]|1[0-9]|20))\\.[1-5])";
    private static final String dateRegex = "((31[\\.](0[13578]|1[02])[\\.](18|19|20)[0-9]{2})|((29|30)[\\.](01|0[3-9]|1[0-2])[\\.](18|19|20)[0-9]{2})|((0[1-9]|1[0-9]|2[0-8])[\\.](0[1-9]|1[0-2])[\\.](18|19|20)[0-9]{2})|(29[\\.](02)[\\.](((18|19|20)(04|08|[2468][048]|[13579][26]))|2000)))";
    private static final String dateToDateRegex = "((31[\\.](0[13578]|1[02])[\\.](18|19|20)[0-9]{2})|((29|30)[\\.](01|0[3-9]|1[0-2])[\\.](18|19|20)[0-9]{2})|((0[1-9]|1[0-9]|2[0-8])[\\.](0[1-9]|1[0-2])[\\.](18|19|20)[0-9]{2})|(29[\\.](02)[\\.](((18|19|20)(04|08|[2468][048]|[13579][26]))|2000)))([\\-]((31[\\.](0[13578]|1[02])[\\.](18|19|20)[0-9]{2})|((29|30)[\\.](01|0[3-9]|1[0-2])[\\.](18|19|20)[0-9]{2})|((0[1-9]|1[0-9]|2[0-8])[\\.](0[1-9]|1[0-2])[\\.](18|19|20)[0-9]{2})|(29[\\.](02)[\\.](((18|19|20)(04|08|[2468][048]|[13579][26]))|2000))))?";
    private static final String queryRegex = "D\\s" +serviceRegex+ "\\s" +questionRegex+ "\\s[PN]\\s" +dateToDateRegex;
    private static final String waitingTimeRegex = "C\\s" +serviceRegex+ "\\s" +questionRegex+ "\\s[PN]\\s" +dateRegex+ "\\s\\d+(\\.\\d+)?";
    private List<String> data;
    private String[] input;

    public Response() {
    }

    public void setInput(String[] input) {
        this.input = input;
    }

    public String[] getResponse(){
        data = filterInput(input);
        List<QueryLine> queryLines = getQueryLines(data);
        List<WaitingTimeLine> waitingTimeLines = getWaitingTimeLines(data);
        String[] response = new String[queryLines.size()];;
        if (queryLines.size() == 0){
            response = new String[]{"-"};
        }

        for (int i = 0; i < queryLines.size(); i++) {
            int result;
            int sum = 0;
            int count = 0;
            for (WaitingTimeLine waitingTimeLine: waitingTimeLines) {
                if (compareLines(queryLines.get(i), waitingTimeLine)){
                    count++;
                    sum+=waitingTimeLine.getResponseTime();
                }
            }

            if (count > 0) {
                result = sum/count;
                response[i] = String.valueOf(result);
            }
            else {
                response[i] = "-";
            }
        }

        return response;
    }

    // Selection queries from input data
    private List<QueryLine> getQueryLines(List<String> data){
        List<QueryLine> queryLines = new ArrayList<>();
        for (int index = 0; index < data.size(); index++) {
            if (data.get(index).startsWith("D")){
                String[] parts = data.get(index).split(" ");
                QueryLine queryLine = new QueryLine();
                queryLine.setIndex(index);
                queryLine.setService(parts[1]);
                queryLine.setQuestion(parts[2]);
                queryLine.setResponseType(parts[3]);
                queryLine.setDate(parts[4]);
                queryLines.add(queryLine);
            }
        }
        return queryLines;
    }

    // Selection waitingtime lines from input data
    private List<WaitingTimeLine> getWaitingTimeLines(List<String> data){
        List<WaitingTimeLine> waitingTimeLines = new ArrayList<>();
        for (int index = 0; index < data.size(); index++) {
            if (data.get(index).startsWith("C")){
                String[] parts = data.get(index).split(" ");
                WaitingTimeLine waitingTime = new WaitingTimeLine();
                waitingTime.setIndex(index);
                waitingTime.setService(parts[1]);
                waitingTime.setQuestion(parts[2]);
                waitingTime.setResponseType(parts[3]);
                waitingTime.setDate(parts[4]);
                waitingTime.setResponseTime(Integer.parseInt(parts[5]));
                waitingTimeLines.add(waitingTime);
            }
        }
        return waitingTimeLines;
    }

    // Discard the line if it does not match to the pattern
    private List<String> filterInput(String[] input){
        int dataSize = Integer.parseInt(input[0]);
        List<String> data = new ArrayList<>(dataSize);

        for (String inp: input){
            if (inp.matches(queryRegex) || inp.matches(waitingTimeRegex)){
                data.add(inp);
            }
        }
        return data;
    }

    // Create Date instance from string representation
    private Date mapDate(String datePart){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(datePart);
        } catch (ParseException e) {
            System.err.println("The date " +datePart+ " cannot be parsed.");
        }
        return date;
    }

    // Check if the query line matches with waitingtime line by specific criteria
    private boolean compareLines(QueryLine query, WaitingTimeLine waitingTime){
        if (query.getIndex() < waitingTime.getIndex()){
            return false;
        }
        if (!query.getResponseType().equals(waitingTime.getResponseType())){
            return false;
        }
        if (!matchByService(query.getService(), waitingTime.getService())){
            return false;
        }
        if (!matchByQuestion(query.getQuestion(), waitingTime.getQuestion())){
            return false;
        }
        if (!matchByDate(query.getDate(), waitingTime.getDate())){
            return false;
        }
        return true;
    }

    private boolean matchByService(String queryService, String waitingTimeService){
        // n.m && n.m
        if (queryService.matches("\\d+\\.\\d") && waitingTimeService.matches("\\d+\\.\\d") && !queryService.equals(waitingTimeService)){
            return false;
        }
        // n && n
        if (queryService.matches("\\d+") && waitingTimeService.matches("\\d+") && !queryService.equals(waitingTimeService)){
            return false;
        }
        // n.m && n
        if (queryService.matches("\\d+\\.\\d") && waitingTimeService.matches("\\d+")){
            return false;
        }
        // n & n.m
        if (queryService.matches("\\d+") && waitingTimeService.matches("\\d+\\.\\d") && !queryService.equals(waitingTimeService.split("\\.")[0])){
            return false;
        }
        if (!queryService.matches(serviceRegex) || !waitingTimeService.matches(serviceRegex)){
            return false;
        }
        return true;
    }

    private boolean matchByQuestion(String queryQuestion, String waitingTimeQuestion){
        // n.m.k && n.m.k
        if (queryQuestion.matches("\\d+\\.\\d+\\.\\d") && waitingTimeQuestion.matches("\\d+\\.\\d+\\.\\d") && !queryQuestion.equals(waitingTimeQuestion)){
            return false;
        }
        // n.m && n.m
        if (queryQuestion.matches("\\d+\\.\\d+") && waitingTimeQuestion.matches("\\d+\\.\\d+") && !queryQuestion.equals(waitingTimeQuestion)){
            return false;
        }
        // n && n
        if (queryQuestion.matches("\\d+") && waitingTimeQuestion.matches("\\d+") && !queryQuestion.equals(waitingTimeQuestion)){
            return false;
        }
        // n && n.m
        if (queryQuestion.matches("\\d+") && waitingTimeQuestion.matches("\\d+\\.\\d+") && !queryQuestion.equals(waitingTimeQuestion.split("\\.")[0])){
            return false;
        }
        // n.m && n.m.k
        if (queryQuestion.matches("\\d+\\.\\d+") && waitingTimeQuestion.matches("\\d+\\.\\d+\\.\\d")){
            String[] query = queryQuestion.split("\\.");
            String[] waiting = waitingTimeQuestion.split("\\.");
            if (!query[0].equals(waiting[0])){
                return false;
            }
            if (query[0].equals(waiting[0]) && !query[1].equals(waiting[1])){
                return false;
            }
        }
        // n && n.m.k
        if (queryQuestion.matches("\\d+") && waitingTimeQuestion.matches("\\d+\\.\\d+\\.\\d") && !queryQuestion.equals(waitingTimeQuestion.split("\\.")[0])){
            return false;
        }
        // n.m.k && n
        if (queryQuestion.matches("\\d+\\.\\d+\\.\\d") && waitingTimeQuestion.matches("\\d+")){
            return false;
        }
        // n.m && n
        if (queryQuestion.matches("\\d+\\.\\d+") && waitingTimeQuestion.matches("\\d+")){
            return false;
        }
        // n.m.k && n.m
        if (queryQuestion.matches("\\d+\\.\\d+\\.\\d") && waitingTimeQuestion.matches("\\d+\\.\\d+")){
            return false;
        }
        if (!queryQuestion.matches(questionRegex) || !waitingTimeQuestion.matches(questionRegex)){
            return false;
        }
        return true;
    }

    private boolean matchByDate(String queryDate, String waitingDate){
        if (queryDate.contains("-")){
            String[] queryDates = queryDate.split("-");

            if (!queryDates[0].matches(dateRegex) || !queryDates[1].matches(dateRegex) || !waitingDate.matches(dateRegex)){
                return false;
            }

            Date dateFrom = mapDate(queryDates[0]);
            Date dateTo = mapDate(queryDates[1]);
            Date responseDate = mapDate(waitingDate);
            if (dateFrom == null || dateTo == null || responseDate == null){
                return false;
            }
            if (dateFrom.compareTo(responseDate) > 0){
                return false;
            }
            if (dateFrom.compareTo(responseDate) < 0 && dateTo.compareTo(responseDate) < 0){
                return false;
            }
        }
        if (!queryDate.contains("-")){
            if (!queryDate.matches(dateRegex) || !waitingDate.matches(dateRegex)){
                return false;
            }

            Date dateFrom = mapDate(queryDate);
            Date responseDate = mapDate(waitingDate);

            if (dateFrom == null || responseDate == null){
                return false;
            }
            if (dateFrom.compareTo(responseDate) > 0){
                return false;
            }
        }
        return true;
    }
}
