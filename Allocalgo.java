import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Allocalgo {

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3307/allocation";

    static final String STUDENT_DB_URL = "jdbc:mariadb://localhost:3307/student";
    static final String STUDENT_USER = "root";
    static final String STUDENT_PASS = "root123";

    class hall_struct {
        char[][] studentAllocation;
        char[][] teacherAllocation;

        hall_struct(int row, int column) {
            studentAllocation = new char[row][column];
            teacherAllocation = new char[row][column];
        }
    }

    class Hall {
        int student_strength = 60;
        int number_of_hall = 2;
        int subjects;
        int capacity = 60; // Hall capacity
        String[] sub;
        int row = 6;
        int column = 10;
        int[] students;
        String[][] teachers;
        hall_struct[] halls;

        Hall(int numberOfHalls, hall_struct[] halls, int[] students, int subjects, String[][] teachers) {
            this.number_of_hall = numberOfHalls;
            this.halls = halls;
            this.students = students;
            this.subjects = subjects;
            this.sub = new String[subjects];
            this.teachers = teachers;
        }

        void Hall_alloc() {
            switch (subjects) {
                case 1:
                    for (hall_struct hallStruct : halls) {
                        int not_allocated = students[0] - (number_of_hall * capacity);
                        int allocated = students[0] - not_allocated;
                        System.out.println("Number of students allocated in hall: " + allocated);
                        System.out.println("Number of students not allocated in hall: " + not_allocated);
                        for (int i = 0; i < row; i++) {
                            for (int j = 0; j < column; j++) {
                                if ((j % 2) == 0 && ((i % 2) == 0)) {
                                    hallStruct.studentAllocation[i][j] = '#';
                                }
                            }
                        }
                    }
                    break;
                case 2:
                case 3:
                case 4:
                    int currentStudent = 0;
                    for (int h = 0; h < number_of_hall; h++) {
                        System.out.println("Hall Number: " + (h + 1));
                        for (int i = 0; i < row; i++) {
                            for (int j = 0; j < column; j++) {
                                if (currentStudent < students[0]) {
                                    int subjectIndex = currentStudent % subjects;
                                    char symbol = getSymbolForSubject(subjectIndex);
                                    hall_struct hallStruct = halls[h];
                                    hallStruct.studentAllocation[i][j] = symbol;
                                    currentStudent++;
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            // Assign teachers to halls
            for (int h = 0; h < number_of_hall; h++) {
                hall_struct hallStruct = halls[h];
                if (teachers != null && teachers.length > h && teachers[h] != null) {
                    String[] teacherPair = teachers[h];
                    if (teacherPair.length >= 2) {
                        // Assign teachers to the top left corner and top right corner of the hall
                        hallStruct.teacherAllocation[0][0] = teacherPair[0].charAt(0); // Assuming teacher name starts with a single character
                        hallStruct.teacherAllocation[0][column - 1] = teacherPair[1].charAt(0); // Assuming teacher name starts with a single character
                    }
                }
            }
        }

        char getSymbolForSubject(int index) {
            switch (index) {
                case 0:
                    return '#'; // Java
                case 1:
                    return '*'; // Python
                case 2:
                    return '%'; // C++
                case 3:
                    return '&'; // JavaScript
                default:
                    return ' ';
            }
        }

        void storeAllocationDetails() {
            Connection conn = null;
            PreparedStatement pstmt = null;
            Connection studentConn = null;
            PreparedStatement studentStmt = null;
            ResultSet studentRs = null;

            try {
                Class.forName("org.mariadb.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, STUDENT_USER, STUDENT_PASS);
                studentConn = DriverManager.getConnection(STUDENT_DB_URL, STUDENT_USER, STUDENT_PASS);

                String studentSql = "SELECT sname FROM student_details";
                studentStmt = studentConn.prepareStatement(studentSql);
                studentRs = studentStmt.executeQuery();

                String sql = "INSERT INTO allocation_details (sname, hallno, seatno) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(sql);

                int seatNumber = 1; // Start seat number from 1
                int studentCount = 0; // Count of students already processed
                int currentHall = 0; // Current hall being processed
                int currentRow = 0; // Current row in the hall being processed
                int currentColumn = 0; // Current column in the hall being processed
                while (studentRs.next()) {
                    String studentName = studentRs.getString("sname");

                    pstmt.setString(1, studentName);
                    pstmt.setInt(2, currentHall + 1); // Hall number starts from 1
                    pstmt.setInt(3, seatNumber); // Seat number
                    pstmt.executeUpdate();

                    hall_struct hall = halls[currentHall];
                    hall.studentAllocation[currentRow][currentColumn] = getSymbolForSubject(studentCount % subjects);

                    currentColumn++;
                    if (currentColumn >= column) {
                        currentColumn = 0;
                        currentRow++;
                        if (currentRow >= row) {
                            currentRow = 0;
                            currentHall++;
                            if (currentHall >= number_of_hall) {
                                break; // All students have been allocated
                            }
                        }
                    }

                    seatNumber++;
                    studentCount++;
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (studentRs != null) studentRs.close();
                    if (studentStmt != null) studentStmt.close();
                    if (studentConn != null) studentConn.close();
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Allocalgo allocalgo = new Allocalgo();
        int numberOfHalls = 2;
        hall_struct[] halls = new hall_struct[numberOfHalls];
        for (int i = 0; i < numberOfHalls; i++) {
            halls[i] = allocalgo.new hall_struct(6, 10);
        }
        // Students and subjects allocation
        int[] students = {60, 60, 60, 60}; // 60 students for each course
        int subjects = 3;
        // Teachers allocation
        String[][] teachers = {{"T1", "T2"}, {"T3", "T4"}};
        Hall h1 = allocalgo.new Hall(numberOfHalls, halls, students, subjects, teachers);
        h1.Hall_alloc();
        h1.storeAllocationDetails();
        for (int i = 0; i < h1.row; i++) {
            for (hall_struct hall : h1.halls) {
                for (int j = 0; j < h1.column; j++) {
                    System.out.print(hall.studentAllocation[i][j] + " ");
                }
                System.out.print("  ");
            }
            System.out.println();
        }
    }
}
