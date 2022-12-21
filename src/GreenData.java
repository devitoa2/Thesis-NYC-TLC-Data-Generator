import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GreenData {

	public static void generateData() throws SQLException {
		NYCLocations LocationMap = new NYCLocations();
		LocationMap.createHashMap();
		Connection conn = DriverManager.getConnection("jdbc:duckdb:");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(
				"SELECT PULocationID, CONCAT(LEFT(CAST(date_trunc('day', lpep_pickup_datetime) as VARCHAR), 8),'01'), "
						+ "CASE WHEN hour(lpep_pickup_datetime) >= 0 AND hour(lpep_pickup_datetime) < 6  THEN 'Overnight' "
						+ "WHEN hour(lpep_pickup_datetime) >= 6 AND hour(lpep_pickup_datetime) < 12  THEN 'Morning' "
						+ "WHEN hour(lpep_pickup_datetime) >= 12 AND hour(lpep_pickup_datetime) < 18  THEN 'Afternoon' "
						+ "WHEN hour(lpep_pickup_datetime) >= 18 AND hour(lpep_pickup_datetime) <= 23  THEN 'Evening' END, COUNT(*), ROUND(AVG(Trip_distance),2), "
						+ "ROUND(AVG(Fare_amount),2), ROUND(AVG(Total_amount),2), FROM "
						+ "'C:\\Users\\Example\\Documents\\resources\\green\\/green*.parquet\' WHERE year(lpep_pickup_datetime) BETWEEN 2015 AND 2022 and PULocationID NOT IN ('264','265') "
						+ "AND Trip_distance<300 GROUP BY PULocationID, CONCAT(LEFT(CAST(date_trunc('day', lpep_pickup_datetime) as VARCHAR), 8),'01'), "
						+ "CASE WHEN hour(lpep_pickup_datetime) >= 0 AND hour(lpep_pickup_datetime) < 6  THEN 'Overnight' "
						+ "WHEN hour(lpep_pickup_datetime) >= 6 AND hour(lpep_pickup_datetime) < 12  THEN 'Morning' "
						+ "WHEN hour(lpep_pickup_datetime) >= 12 AND hour(lpep_pickup_datetime) < 18  THEN 'Afternoon' "
						+ "WHEN hour(lpep_pickup_datetime) >= 18 AND hour(lpep_pickup_datetime) <= 23  THEN 'Evening' END ORDER BY "
						+ "CONCAT(LEFT(CAST(date_trunc('day', lpep_pickup_datetime) as VARCHAR), 8),'01'), "
						+ "CASE WHEN hour(lpep_pickup_datetime) >= 0 AND hour(lpep_pickup_datetime) < 6  THEN 'Overnight' "
						+ "WHEN hour(lpep_pickup_datetime) >= 6 AND hour(lpep_pickup_datetime) < 12  THEN 'Morning' "
						+ "WHEN hour(lpep_pickup_datetime) >= 12 AND hour(lpep_pickup_datetime) < 18  THEN 'Afternoon' "
						+ "WHEN hour(lpep_pickup_datetime) >= 18 AND hour(lpep_pickup_datetime) <= 23  THEN 'Evening' END, PULocationID");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Example\\Documents\\greendata.sql"));
			out.write(
					"INSERT INTO \"GREEN_TRIPS\" (taxi_zone, pulocationid, pickup_date, pickup_time, total_trips, trip_distance, fare_amount, total_amount) VALUES");
			out.newLine();
			while (rs.next()) {
				String r1 = rs.getString(1);
				String r2 = rs.getString(1);
				r2 = LocationMap.translate(r2);
				String r3 = rs.getString(2);
				String r4 = rs.getString(3);
				String r5 = rs.getString(4);
				String r6 = rs.getString(5);
				String r7 = rs.getString(6);
				String r8 = rs.getString(7);

				out.write("('" + r1 + "', '" + r2 + "', '" + r3 + "', '" + r4 + "', '" +
						r5 + "', '" + r6 + "', '" + r7 + "', '" + r8 + "'),");
				out.newLine();
			}
			System.out.println("Completed writing into text file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
