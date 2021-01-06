package DB_lecture_example;
import DB_lecture_example.DatabaseAuthInformation;

import java.sql.*;
import java.util.Scanner;

public class lecture_example {
    public static void main(String[] argv) throws SQLException {
        /* Retrieve DB authentication information */
        DatabaseAuthInformation db_info = new DatabaseAuthInformation();
        db_info.parse_auth_info("auth/mysql.auth");
        /* Prepare the URL for DB connection */
        String db_connection_url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowMultiQueries=true", db_info.getHost(),
                db_info.getPort(),
                db_info.getDatabase_name());

        /* Table Properties */
        String createTableSQL = "create table 채팅 "
                + "(개설자고유식별번호 int(8) not null, "
                + "발신자고유식별번호 int(8) not null, "
                + "발신시간 char(8) not null, "
                + "내용 varchar(100) not null, "
                + "수신자고유식별번호 int(8) not null, "
                + "constraint chatKey primary key(개설자고유식별번호, 발신자고유식별번호, 발신시간));";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            /* Connection with MySQL */
            Connection db_connection = DriverManager.getConnection(db_connection_url, db_info.getUsername(), db_info.getPassword());

            Statement st = db_connection.createStatement();
            st.executeUpdate(createTableSQL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int makeId[] = {71829384, 71829384};
        int sendId[] = {39281740, 29384372};
        String sendTime[] = {"12:34:00", "12:34:21"};
        String content [] = {"안녕하세요 반갑습니다!", "저도 만나서 반갑습니다"};
        int recId[] = {29384372, 39281740};


        /* Add route*/
        for(int i = 0 ; i < 2 ; i++) {
            String query_string = "insert into 채팅(개설자고유식별번호, 발신자고유식별번호, 발신시간, 내용, 수신자고유식별번호) VALUES (?, ?, ?, ?, ?)";
            /* DB insertion process */
            try (Connection db_connection = DriverManager.getConnection(db_connection_url,
                    db_info.getUsername(),
                    db_info.getPassword());
                 PreparedStatement db_statement = db_connection.prepareStatement(query_string)) {
                /* Set the query statement */
                db_statement.setInt(1, makeId[i]);
                db_statement.setInt(2, sendId[i]);
                db_statement.setString(3, sendTime[i]);
                db_statement.setString(4, content[i]);
                db_statement.setInt(5, recId[i]);
                /* Send query and get the result */
                int result = db_statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("where절 조건을 입력하시오: ");
            String where = scanner.nextLine();

            Connection db_connection = DriverManager.getConnection(db_connection_url,
                    db_info.getUsername(), db_info.getPassword());
            Statement stmt = db_connection.createStatement();

            String query_string =
                    "SELECT 개설자고유식별번호, 발신자고유식별번호, 발신시간, 내용, 수신자고유식별번호 "
                    + "FROM 채팅 "
                    + "WHERE " + where ;

            ResultSet rs = stmt.executeQuery(query_string);
            try {
                while (rs.next()) {
                    System.out.print(rs.getInt("개설자고유식별번호") + " ");
                    System.out.print(rs.getInt("발신자고유식별번호") + " ");
                    System.out.print(rs.getString("발신시간") + " ");
                    System.out.print(rs.getString("내용") + " ");
                    System.out.println(rs.getInt("수신자고유식별번호") + " ");
                }
            } catch (SQLException e) {}
        }
    }
}
