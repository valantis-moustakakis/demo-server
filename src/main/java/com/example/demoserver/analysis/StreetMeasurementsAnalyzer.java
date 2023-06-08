package com.example.demoserver.analysis;

import com.example.demoserver.entities.StreetInfoEntity;
import com.example.demoserver.repositories.StreetInfoRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.lag;

@Slf4j
@RequiredArgsConstructor
public class StreetMeasurementsAnalyzer {

    private final Gson gson;
    private final StreetInfoRepository streetInfoRepository;

    // TODO: define this limit
    private static final int recordableLimit = 20;

    public void analyzeStreetMeasurements() {
        SparkSession spark = SparkSession.builder()
                .appName("AccelerometerDataAnalysis")
                .master("local[*]")
                .getOrCreate();

        // Step 1: Read Data
        Dataset<Row> data = spark.read()
                .format("jdbc")
                .option("url", "jdbc:mysql://localhost:3306/thesis")
                .option("dbtable", "demo_street_measurements")
                .option("user", "root")
                .option("password", "root")
                .load();

        WindowSpec windowSpec = Window.partitionBy("user_id").orderBy("ts"); // Update window specification

        Dataset<Row> transformedData = data
                .withColumn("prev_accel_x", lag(col("accelerometer_x"), 1).over(windowSpec))
                .withColumn("prev_accel_y", lag(col("accelerometer_y"), 1).over(windowSpec))
                .withColumn("prev_accel_z", lag(col("accelerometer_z"), 1).over(windowSpec))
                .withColumn("accel_x_diff", col("accelerometer_x").minus(col("prev_accel_x")))
                .withColumn("accel_y_diff", col("accelerometer_y").minus(col("prev_accel_y")))
                .withColumn("accel_z_diff", col("accelerometer_z").minus(col("prev_accel_z")));

        transformedData.createOrReplaceTempView("transformedData"); // Create a temporary view for the transformed data

        // TODO: define these limits
        Dataset<Row> locationsLow = getRowDataset(spark, 0.5f, 1);
        Dataset<Row> locationsMedium = getRowDataset(spark, 1.01f, 1.75f);
        Dataset<Row> locationsHigh = getRowDataset(spark, 1.76f, 10);

        recordResults(locationsLow, "LOW");
        recordResults(locationsMedium, "MEDIUM");
        recordResults(locationsHigh, "HIGH");

        spark.stop();
    }

    private Dataset<Row> getRowDataset(SparkSession spark, float min, float max) {
        Dataset<Row> filteredData = spark.sql(
                "WITH filteredData AS (" +
                        "SELECT *, lag(user_id) OVER (PARTITION BY user_id ORDER BY ts) AS prev_user_id " +
                        "FROM transformedData" +
                        ") " +
                        "SELECT * " +
                        "FROM filteredData " +
                        "WHERE user_id = prev_user_id " +
                        "AND (" +
                        "   (accel_x_diff > " + min + " AND accel_x_diff < " + max + ") OR (accel_x_diff < -" + min + " AND accel_x_diff > -" + max + ")" +
                        "   OR (accel_y_diff > " + min + " AND accel_y_diff < " + max + ") OR (accel_y_diff < -" + min + " AND accel_y_diff > -" + max + ")" +
                        "   OR (accel_z_diff > " + min + " AND accel_z_diff < " + max + ") OR (accel_z_diff < -" + min + " AND accel_z_diff > -" + max + ")" +
                        ")");

        return filteredData.select("longitude", "latitude");
    }

    private void recordResults(Dataset<Row> locations, String severity) {
        List<Row> rows = locations.collectAsList();
        Map<String, Integer> groupingMap = new HashMap<>();
        for (Row row : rows) {
            groupingMap.merge(row.json(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : groupingMap.entrySet()) {
            if (entry.getValue() > recordableLimit) {
                Coordinates coordinates = gson.fromJson(entry.getKey(), Coordinates.class);
                StreetInfoEntity entity = new StreetInfoEntity(severity, coordinates.getLatitude(), coordinates.getLongitude());
                streetInfoRepository.save(entity);
            }
        }
    }
}
