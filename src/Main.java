import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(args[0]);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line = bufferedReader.readLine();
        String[] strings = line.substring(1,line.length()-1).trim().split(",");
        Integer[] integers = new Integer[strings.length];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = Integer.valueOf(strings[i].trim());
        }
        Integer[] answer = new Integer[5];
        // count of elements
        answer[0] = integers.length;
        //count of unique elements
        Set<Integer> set = new HashSet<>(Arrays.asList(integers));
        answer[1] = set.size();
        //other three tasks
        int once = 0;
        int maxCount = 0, maxNumber = 0;
        for (Integer integer1 : integers) {
            int count = 0;
            for (Integer integer : integers) {
                if (integer1.equals(integer)) count++;
            }
            if (count == 1) once++;
            if (count > maxCount) {
                maxCount = count;
                maxNumber = integer1;
            }
        }
        answer[2] = once;
        answer[3] = maxNumber;
        answer[4] = maxCount;
        System.out.println(Arrays.toString(answer));


        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/test",
                            "postgres", "159482673");
            c.setAutoCommit(false);

            String sql = "INSERT INTO checkHistory (CheckTime,InputData,OutputData) "
                    + "VALUES (?, ?, ?);";
            PreparedStatement pstmt = c.prepareStatement(sql);

            java.util.Date date = new Date();
            pstmt.setTimestamp(1,new Timestamp(date.getTime()));
            Array in = c.createArrayOf("integer",integers);
            pstmt.setArray(2,in);
            Array out = c.createArrayOf("integer",answer);
            pstmt.setArray(3,out);

            pstmt.executeUpdate();
            c.commit();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
}
