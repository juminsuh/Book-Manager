import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.time.Year;
import java.util.Scanner;

public class BookManager {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
        System.out.println("환영합니다, 관리자님!\n");
        System.out.println("1. book_genre: 15권이 넘는 장르별 도서 수 조회");
        System.out.println("2. loan_list: 회원별 대출 중인 도서 목록 조회");
        System.out.println("3. longest_penalty: 2025년 최장 연체 기간 초과 연체일 기록 조회");
        System.out.println("4. reader_rank: 회원별 대출 순위 매기기");
        System.out.println("5. publisher_book: 장르와 출판 연도를 기준으로 계층적 도서 수 조회");
        System.out.println("6. insert_book: 새로운 도서 등록");
        System.out.println("7. insert_mem: 새로운 회원 등록");
        System.out.println("8. delete_publisher: 특정 출판사 도서 삭제");
        System.out.println("9. update_mem: 회원 전화번호 변경");
        System.out.println("0. exit: 프로그램 종료\n");

        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            System.out.print("\n위 메뉴에서 원하시는 명령어의 번호를 입력하세요 (종료하려면 0 또는 exit 입력): ");
            String command = scanner.nextLine().trim();

            switch (command.toLowerCase()) {
                case "1":
                    bookGenre();
                    break;
                case "2":
                    loanList();
                    break;
                case "3":
                    longestPenalty();
                    break;
                case "4":
                    readerRank();
                    break;
                case "5":
                    publisherBook();
                    break;
                case "6":
                    insertBook(scanner);
                    break;
                case "7":
                    insertMember(scanner);
                    break;
                case "8":
                    deletePublisher(scanner);
                    break;
                case "9":
                    updateMem(scanner);
                    break;
                case "0":
                case "exit":
                    running = false;
                    System.out.println("프로그램을 종료합니다.");
                    break;
                default:
                    System.out.println("알 수 없는 명령입니다: " + command);
            }
        }

        scanner.close();
    }

		// 1. book_genre: 장르별 도서 수 조회
		public static void bookGenre() {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        Statement myState = null;
	        ResultSet myResSet = null;
	        
			String sql_select = "";
	        // book
	        String genre = "";

	        try {
	            // 데이터베이스 연결 
	            myConn = DriverManager.getConnection(url, userID, userPW);
	            System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
	            System.out.println("데이터베이스에 접근합니다.");

	            // statement 객체 생성
	            myState = myConn.createStatement();
	            sql_select = "select genre, count(*) as book_count from book group by genre having count(*) > 15;";

	            // 쿼리를 DB에 전송 및 실행
	            myResSet = myState.executeQuery(sql_select);

	            System.out.println("15권이 넘는 장르의 책 권수를 조회합니다.\n");
	            
	            while (myResSet.next()) {
	                genre = myResSet.getString("genre");
	                int bookCount = myResSet.getInt("book_count");
	                System.out.println("장르: " + genre + ", 책 권수: " + bookCount);
	            }


	        } catch (SQLException e) {
	            // System.out.println("SQL 예외 발생: " + e.getMessage());
	        	e.printStackTrace();
	        } finally {
	        	
	            if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myState != null) {
	                try {
	                    myState.close();
	                    System.out.println("...Close Statement...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myConn != null) {
	                try {
	                	myConn.close();
	                    System.out.println("...Close Connection " + myConn.toString() + "...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        
	        System.out.println("장르별 도서 수 조회 완료.");
			
		}
		
		// 2. loan_list: 회원별 대출 중인 도서 목록 조회
		public static void loanList() {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        Statement myState = null;
	        ResultSet myResSet = null;
	        
	        String sql_select2 = "";

	        // book
	        String book_id = "";
	        String title = "";
	        
	        // member
	        int member_id = 0;
	        String mname = "";
	        
	        // loan
	        int loan_id = 0;
	        Date loan_date = null;
	        Date return_date = null;
			

	        try {
	            // 데이터베이스 연결 
	            myConn = DriverManager.getConnection(url, userID, userPW);
	            System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
	            System.out.println("데이터베이스에 접근합니다.");

	            // statement 객체 생성
	            myState = myConn.createStatement();
	            sql_select2 = "select member.member_id, mname, book.book_id, title, loan_id, loan_date, return_date " +
	                    "from member inner join loan " +
	                    "on member.member_id = loan.member_id " +
	                    "inner join book " +
	                    "on loan.book_id = book.book_id " +
	                    "order by member.member_id;";

	            // 쿼리를 DB에 전송 및 실행
	            myResSet = myState.executeQuery(sql_select2);
	            System.out.println("회원별 대출 중인 도서 목록을 조회합니다.\n");
	            System.out.println("회원번호" + " | " + "회원이름" + " | "
                        + "책번호" + " | " + "제목" + " | " + "대출번호" + " | " + "대출날짜" + " | " + "반납날짜");
	            
	            while (myResSet.next()) {
	            	member_id = myResSet.getInt("member_id");
	            	mname = myResSet.getString("mname");
	                book_id = myResSet.getString("book_id");
	                title = myResSet.getString("title");
	                loan_id = myResSet.getInt("loan_id");
	                loan_date = myResSet.getDate("loan_date");
	                return_date = myResSet.getDate("return_date");

	                System.out.println(member_id + " | " + mname + " | "
	                        + book_id + " | " + title + " | " + loan_id + " | " + loan_date + "|" + return_date);
	                
	            }

	        } catch (SQLException e) {
	            System.out.println("SQL 예외 발생: " + e.getMessage());

	        } finally {
	            if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myState != null) {
	                try {
	                    myState.close();
	                    System.out.println("...Close Statement...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myConn != null) {
	                try {
	                    System.out.println("...Close Connection " + myConn.toString() + "...");
	                    myConn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        
	        System.out.println("회원별 대출 중인 도서 목록 조회 완료.");
			
		}
		
		// 3. longest_penalty: 2025년 최장 연체 기간 초과 연체일 기록 조회
		public static void longestPenalty() {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        Statement myState = null;
	        ResultSet myResSet = null;
	        
	        String sql_subquery = "";
			
	        // loan
	        String book_id = null;
	        int member_id = 0;
	        
	        // penalty
	        int penalty_id = 0;
	        int penalty_date = 0;

	        try {
	        	
	            // 데이터베이스 연결 
	            myConn = DriverManager.getConnection(url, userID, userPW);
	            System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
	            System.out.println("데이터베이스에 접근합니다.");

	            // statement 객체 생성
	            myState = myConn.createStatement();
	            sql_subquery = "select penalty_id, book_id, member_id, penalty_date\n"
	            		+ "from loan inner join penalty\n"
	            		+ "on loan.loan_id = penalty.loan_id\n"
	            		+ "where penalty_date > (select max(penalty_date)\n"
	            		+ "from loan inner join penalty\n"
	            		+ "on loan.loan_id = penalty.loan_id\n"
	            		+ "where year(loan_date) = 2025);";
	            // 쿼리를 DB에 전송 및 실행
	            myResSet = myState.executeQuery(sql_subquery);
	            System.out.println("2025년 최장 연체 기간 초과 연체일 기록을 조회합니다.\n");
	            System.out.println("연체번호" + " | " + "책번호" + " | "
                        + "회원번호" + " | " + "연체날짜");

	            while (myResSet.next()) {
	            	penalty_id = myResSet.getInt("penalty_id");
	                book_id = myResSet.getString("book_id");
	                member_id = myResSet.getInt("member_id");
	                penalty_date = myResSet.getInt("penalty_date");

	                System.out.println(penalty_id + " | " + book_id + " | "
	                        + member_id + " | " + penalty_date);
	                
	            }

	        } catch (SQLException e) {
	            System.out.println("SQL 예외 발생: " + e.getMessage());

	        } finally {
	            if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myState != null) {
	                try {
	                    myState.close();
	                    System.out.println("...Close Statement...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myConn != null) {
	                try {
	                    System.out.println("...Close Connection " + myConn.toString() + "...");
	                    myConn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        
	       System.out.println("2025년 최장 연체 기간 초과 연체일 기록 조회 완료.");
			
		}

		// 4. reader_rank: 회원별 대출 순위 매기기 (윈도우 기반)
		public static void readerRank() {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        Statement myState = null;
	        ResultSet myResSet = null;
	        
			String sql_window = "";
			
			int memberId = 0;
			int numRead = 0;
			int rank = 0;

	        try {
	            // 데이터베이스 연결 
	            myConn = DriverManager.getConnection(url, userID, userPW);
	            System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
	            System.out.println("데이터베이스에 접근합니다.");

	            // statement 객체 생성
	            myState = myConn.createStatement();
	            sql_window = "select member_id, num_read, dense_rank() over (order by num_read desc) as rank_read\n"
	            		+ "from most_reader;";

	            // 쿼리를 DB에 전송 및 실행
	            myResSet = myState.executeQuery(sql_window);
	            System.out.println("회원별 대출 순위를 조회합니다.\n");
	            
	            while (myResSet.next()) {
	                memberId = myResSet.getInt("member_id");
	                numRead = myResSet.getInt("num_read");
	                rank = myResSet.getInt("rank_read");

	                System.out.println("회원 ID: " + memberId + ", 대출 횟수: " + numRead + ", 순위: " + rank);
	            }

	        } catch (SQLException e) {
	            // System.out.println("SQL 예외 발생: " + e.getMessage());
	        	e.printStackTrace();
	        } finally {
	        	
	            if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myState != null) {
	                try {
	                    myState.close();
	                    System.out.println("...Close Statement...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myConn != null) {
	                try {
	                	myConn.close();
	                    System.out.println("...Close Connection " + myConn.toString() + "...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        
	        System.out.println("회원별 대출 순위 매기기 완료.");
			
		}
		
		// 5. publisher_book: 장르와 출판 연도를 기준으로 계층적 도서 수 조회 (다차원)
		public static void publisherBook() {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        Statement myState = null;
	        ResultSet myResSet = null;
	        
			String rollupSQL = "";
			
			String genreVal = "";
			int yearVal = 0;
			int count = 0;

	        try {
	            // 데이터베이스 연결 
	            myConn = DriverManager.getConnection(url, userID, userPW);
	            System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
	            System.out.println("데이터베이스에 접근합니다.");

	            // statement 객체 생성
	            myState = myConn.createStatement();
	            rollupSQL = "select genre, published_year, count(*) as book_count "
                        + "from book "
                        + "group by genre, published_year with rollup;";

	            // 쿼리를 DB에 전송 및 실행
	            myResSet = myState.executeQuery(rollupSQL);
	            System.out.println("장르와 출판 연도를 기준으로 계층적 도서 수를 조회합니다.\n");
	            
	            while (myResSet.next()) {
	                genreVal = myResSet.getString("genre");
	                yearVal = myResSet.getInt("published_year");
	                boolean isYearNull = myResSet.wasNull();

	                count = myResSet.getInt("book_count");

	                String genreDisplay = (genreVal == null) ? "전체 장르 합계" : genreVal;
	                String yearDisplay;

	                if (genreVal != null && isYearNull) {
	                    yearDisplay = "장르별 총합";
	                } else if (genreVal == null && isYearNull) {
	                    yearDisplay = "전체 총합";
	                } else {
	                    yearDisplay = String.valueOf(yearVal);
	                }
	                System.out.println("장르: " + genreDisplay + ", 출판 연도: " + yearDisplay + ", 도서 수: " + count);
	            }

	        } catch (SQLException e) {
	            // System.out.println("SQL 예외 발생: " + e.getMessage());
	        	e.printStackTrace();
	        } finally {
	        	
	            if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myState != null) {
	                try {
	                    myState.close();
	                    System.out.println("...Close Statement...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (myConn != null) {
	                try {
	                	myConn.close();
	                    System.out.println("...Close Connection " + myConn.toString() + "...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        
	        System.out.println("장르와 출판 연도를 기준으로 계층적 도서 수 조회 완료.");
			
		}
		
		
		
		// 6. insert_book: 새로운 도서 등록
		public static void insertBook(Scanner scanner) {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        ResultSet myResSet = null;
	        PreparedStatement pstmt = null;
	        
	        String sql_insert1 = "";
						
			System.out.println("도서 ID를 입력하세요: ");
			String bookId = scanner.nextLine();
			
			System.out.println("도서 제목을 입력하세요: ");
			String title = scanner.nextLine();
			
			System.out.println("도서 저자를 입력하세요: ");
			String author = scanner.nextLine();
			
			System.out.println("도서 출판사를 입력하세요: ");
			String publisher = scanner.nextLine();
			
			System.out.println("도서 출판연도를 입력하세요: ");
			int published_year = Integer.parseInt(scanner.nextLine());
			
			System.out.println("도서 장르를 입력하세요 (소설, 시/에세이, 자기계발, IT/컴퓨터, 경제/경영, 여행): ");
			String genre = scanner.nextLine();

	        try {
				// DB연결
				myConn = DriverManager.getConnection(url, userID, userPW);
				System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
				System.out.println("데이터베이스에 접근합니다.");
				
				// myConn.setAutoCommit(false); // 트랜잭션 시작
				
				sql_insert1 = "insert into book (book_id, title, author, publisher, published_year, genre) values (?,?,?,?,?,?);";
				pstmt = myConn.prepareStatement(sql_insert1); // 쿼리 구조 전송
				
				pstmt.setString(1, bookId);
				pstmt.setString(2, title);
				pstmt.setString(3, author);
				pstmt.setString(4, publisher);
				pstmt.setInt(5, published_year);
				pstmt.setString(6, genre);
				
				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("도서가 성공적으로 등록되었습니다.\n");
				/*
				// Rollback
				myConn.rollback();
				System.out.println("롤백 완료: 도서 등록이 취소 되었습니다.");
				*/
				}
			} catch (SQLException e) {
				System.out.println("오류 발생: " + e.getMessage());
	            e.printStackTrace();
			} finally {
				if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
				}
				if (pstmt != null) {
					try {
						pstmt.close();
						System.out.println("...Close Statement...");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (myConn != null) {
					try {
						System.out.println("...Close Connection " + myConn.toString() + "...");
						myConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}	
	        
	        System.out.println("새로운 도서 등록 완료.");
			
		}
		
		// 7. insert_mem: 새로운 회원 등록
		public static void insertMember(Scanner scanner) {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        ResultSet myResSet = null;
	        PreparedStatement pstmt = null;
	        
	        String sql_insert2 = "";
			
			System.out.println("회원 ID를 입력하세요: ");
			int memberId = Integer.parseInt(scanner.nextLine());
			
			System.out.println("회원 이름을 입력하세요: ");
			String mname = scanner.nextLine();
			
			System.out.println("회원 이메일을 입력하세요: ");
			String email = scanner.nextLine();
			
			System.out.println("회원 전화번호를 입력하세요: ");
			String phone = scanner.nextLine();

			try {
				// DB연결
				myConn = DriverManager.getConnection(url, userID, userPW);
				// myConn.setAutoCommit(false); // 자동 커밋 끄기 (트랜젝션 시작)
				System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
				System.out.println("데이터베이스에 접근합니다.");
				
				// 회원등록 Insert
				sql_insert2 = "insert into member (member_id, mname, email, phone) values (?, ?, ?, ?)";
				pstmt = myConn.prepareStatement(sql_insert2);

				pstmt.setInt(1, memberId);
				pstmt.setString(2, mname);
				pstmt.setString(3, email);
				pstmt.setString(4, phone);

				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted > 0) {
				    System.out.println("회원가입이 완료되었습니다.");
				}
				/*
				// ROLLBACK 수행 (DB에는 실제로 반영되지 않음)
				myConn.rollback();
				System.out.println("롤백 완료: 회원 등록이 취소되었습니다.");
				*/
				
			} catch (SQLException e) {
				System.out.println("오류 발생: " + e.getMessage());
	            e.printStackTrace();
			} finally {
				if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
				}
				if (pstmt != null) {
					try {
						pstmt.close();
						System.out.println("...Close Statement...");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (myConn != null) {
					try {
						System.out.println("...Close Connection " + myConn.toString() + "...");
						myConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
			System.out.println("새로운 회원 등록 완료.");		
			
			
		}
		
		// 8. delete_publisher: 특정 출판사 도서 삭제
		public static void deletePublisher(Scanner scanner) {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        ResultSet myResSet = null;
	        PreparedStatement pstmt = null;
	        
	        String sql_delete = "";
			
			System.out.println("출판사 명칭을 입력하세요: ");
			String publisherName = scanner.nextLine();

			try {
				// DB연결
				myConn = DriverManager.getConnection(url, userID, userPW);
				// myConn.setAutoCommit(false); // 자동 커밋 끄기 (트랜젝션 시작)
				System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
				System.out.println("데이터베이스에 접근합니다.");
				
				// 출판사 Delete
				sql_delete = "delete from book where publisher = ?;";
				pstmt = myConn.prepareStatement(sql_delete);

				pstmt.setString(1, publisherName);
				
				int rowsDeleted = pstmt.executeUpdate();

				if (rowsDeleted > 0) {
				    System.out.println("해당 출판사의 책 정보 삭제가 완료되었습니다. 삭제된 행 수: " + rowsDeleted);
				} else {
				    System.out.println("해당 출판사의 책이 존재하지 않습니다. 삭제된 항목 없음.");
				}
				
				/*
				// ROLLBACK 수행 (DB에는 실제로 반영되지 않음)
				myConn.rollback();
				System.out.println("롤백 완료: 출판사 삭제가 취소되었습니다.");
				*/
				
			} catch (SQLException e) {
				System.out.println("오류 발생: " + e.getMessage());
	            e.printStackTrace();
			} finally {
				if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
				}
				if (pstmt != null) {
					try {
						pstmt.close();
						System.out.println("...Close Statement...");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (myConn != null) {
					try {
						System.out.println("...Close Connection " + myConn.toString() + "...");
						myConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
			System.out.println("출판사 삭제 완료.");		
			
			
		}
		
		// 9. update_mem: 회원 전화번호 변경
		public static void updateMem(Scanner scanner) {
			
	        String userID = "testuser";
	        String userPW = "testpw";
	        String dbName = "dbjaja";
	        String header = "jdbc:mysql://localhost:3306/";
	        String encoding = "useUnicode=true&characterEncoding=UTF-8";
	        String url = header + dbName + "?" + encoding;

	        Connection myConn = null;
	        ResultSet myResSet = null;
	        PreparedStatement pstmt = null;
	        
	        String sql_update = "";
	        
	        System.out.println("변경을 원하는 회원번호를 입력해주세요: ");
	        int memberId = Integer.parseInt(scanner.nextLine());
			
			System.out.println("회원 이름을 입력해주세요: ");
			String m_name = scanner.nextLine();
			
	        System.out.println("변경할 전화번호를 입력해주세요: ");
			String phoneNum = scanner.nextLine();

			try {
				// DB연결
				myConn = DriverManager.getConnection(url, userID, userPW);
				// myConn.setAutoCommit(false); // 자동 커밋 끄기 (트랜젝션 시작)
				System.out.println("...Connected to the database " + dbName + " in MySQL with " + myConn.toString() + "...");
				System.out.println("데이터베이스에 접근합니다.");
				
				// 전화번호 update
				sql_update = "update member set phone =? where member_id = ? and mname = ?";
				pstmt = myConn.prepareStatement(sql_update); // 쿼리 구조 전송
				
				pstmt.setString(1, phoneNum);
				pstmt.setInt(2, memberId);
				pstmt.setString(3, m_name);
				
				int rowsAffected = pstmt.executeUpdate();

				if (rowsAffected > 0) {
				    System.out.println("회원 정보 변경이 완료되었습니다.");
				} else {
				    System.out.println("일치하는 회원 정보가 없습니다. 변경되지 않았습니다.");
				}
				
				/*
				// ROLLBACK 수행 (DB에는 실제로 반영되지 않음)
				myConn.rollback();
				System.out.println("롤백 완료: 회원 정보 변경이 취소되었습니다.");
				*/
				
			} catch (SQLException e) {
				System.out.println("오류 발생: " + e.getMessage());
	            e.printStackTrace();
			} finally {
				if (myResSet != null) {
	                try {
	                    myResSet.close();
	                    System.out.println("...Close ResultSet...");
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
				}
				if (pstmt != null) {
					try {
						pstmt.close();
						System.out.println("...Close Statement...");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (myConn != null) {
					try {
						System.out.println("...Close Connection " + myConn.toString() + "...");
						myConn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
			System.out.println("회원 정보 변경 완료.");		
			
			
		}

		
		
		
}