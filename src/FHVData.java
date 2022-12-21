import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FHVData {

	public static void generateData() throws SQLException {
		NYCLocations LocationMap = new NYCLocations();
		LocationMap.createHashMap();
		Connection conn = DriverManager.getConnection("jdbc:duckdb:");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(
				"SELECT left(PULocationID,position('.' in PULocationID)-1), CONCAT(LEFT(CAST(date_trunc('day', Pickup_datetime ) as VARCHAR), 8),'01'), "
						+ "CASE WHEN hour(Pickup_datetime ) >= 0 AND hour(Pickup_datetime ) < 6  THEN 'Overnight' "
						+ "WHEN hour(Pickup_datetime ) >= 6 AND hour(Pickup_datetime ) < 12  THEN 'Morning' "
						+ "WHEN hour(Pickup_datetime ) >= 12 AND hour(Pickup_datetime ) < 18  THEN 'Afternoon' "
						+ "WHEN hour(Pickup_datetime ) >= 18 AND hour(Pickup_datetime ) <= 23  THEN 'Evening' END, COUNT(*) FROM "
						+ "'C:\\Users\\Example\\Documents\\resources\\fhv\\/fhv*.parquet\' WHERE year(Pickup_datetime ) BETWEEN 2015 AND 2022 AND "
						+ "left(PULocationID,position('.' in PULocationID)-1) NOT IN ('0','264','265') "
						+ "GROUP BY left(PULocationID,position('.' in PULocationID)-1), CONCAT(LEFT(CAST(date_trunc('day', Pickup_datetime ) as VARCHAR), 8),'01'), "
						+ "CASE WHEN hour(Pickup_datetime ) >= 0 AND hour(Pickup_datetime ) < 6  THEN 'Overnight' "
						+ "WHEN hour(Pickup_datetime ) >= 6 AND hour(Pickup_datetime ) < 12  THEN 'Morning' "
						+ "WHEN hour(Pickup_datetime ) >= 12 AND hour(Pickup_datetime ) < 18  THEN 'Afternoon' "
						+ "WHEN hour(Pickup_datetime ) >= 18 AND hour(Pickup_datetime ) <= 23  THEN 'Evening' END ORDER BY "
						+ "CONCAT(LEFT(CAST(date_trunc('day', Pickup_datetime ) as VARCHAR), 8),'01'), "
						+ "CASE WHEN hour(Pickup_datetime ) >= 0 AND hour(Pickup_datetime ) < 6  THEN 'Overnight' "
						+ "WHEN hour(Pickup_datetime ) >= 6 AND hour(Pickup_datetime ) < 12  THEN 'Morning' "
						+ "WHEN hour(Pickup_datetime ) >= 12 AND hour(Pickup_datetime ) < 18  THEN 'Afternoon' "
						+ "WHEN hour(Pickup_datetime ) >= 18 AND hour(Pickup_datetime ) <= 23  THEN 'Evening' END, CAST(left(PULocationID,position('.' in PULocationID)-1) as INTEGER)");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Example\\Documents\\fhvdata.sql"));
			out.write(
					"INSERT INTO \"FHVTRIPS\" (taxi_zone, pulocationid, pickup_date, pickup_time, total_trips) VALUES");
			out.newLine();
			while (rs.next()) {
				String r1 = rs.getString(1);
				String r2 = LocationMap.translate(r1);
				String r3 = rs.getString(2);
				String r4 = rs.getString(3);
				String r5 = rs.getString(4);
				out.write("('" + r1 + "', '" + r2 + "', '" + r3 + "', '" + r4 + "', '" + r5 + "'),");
				out.newLine();
			}
			System.out.println("Completed writing into text file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
