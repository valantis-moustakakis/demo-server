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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.lag;

@Slf4j
@RequiredArgsConstructor
@Service
public class StreetMeasurementsAnalyzer {

    private final Gson gson;
    private final StreetInfoRepository streetInfoRepository;

    @Value("${analysis.sum.threshold}")
    private static int recordableLimit;

    private SparkSession spark;

    @Value("${analysis.smooth.min.limit}")
    private float smoothMinLimit;
    @Value("${analysis.smooth.max.limit}")
    private float smoothMaxLimit;
    @Value("${analysis.low.min.limit}")
    private float lowMinLimit;
    @Value("${analysis.low.max.limit}")
    private float lowMaxLimit;
    @Value("${analysis.medium.min.limit}")
    private float mediumMinLimit;
    @Value("${analysis.medium.max.limit}")
    private float mediumMaxLimit;
    @Value("${analysis.high.min.limit}")
    private float highMinLimit;
    @Value("${analysis.high.max.limit}")
    private float highMaxLimit;

    public void analyzeStreetMeasurements() {
        spark = SparkSession.builder()
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

        Dataset<Row> locationsSmooth = getRowDataset(smoothMinLimit, smoothMaxLimit);
        Dataset<Row> locationsLow = getRowDataset(lowMinLimit, lowMaxLimit);
        Dataset<Row> locationsMedium = getRowDataset(mediumMinLimit, mediumMaxLimit);
        Dataset<Row> locationsHigh = getRowDataset(highMinLimit, highMaxLimit);

        recordResults(locationsHigh, "HIGH");
        recordResults(locationsMedium, "MEDIUM");
        recordResults(locationsLow, "LOW");
        recordResults(locationsSmooth, "NO_SEVERITY");

        spark.stop();
    }

    private Dataset<Row> getRowDataset(float min, float max) {
        Dataset<Row> filteredData = spark.sql(
                "WITH filteredData AS (" +
                        "SELECT *, lag(user_id) OVER (PARTITION BY user_id ORDER BY ts) AS prev_user_id " +
                        "FROM transformedData" +
                        ") " +
                        "SELECT * " +
                        "FROM filteredData " +
                        "WHERE user_id = prev_user_id " +
                        "AND (" +
                        "   (accel_x_diff >= " + min + " AND accel_x_diff <= " + max + ") OR (accel_x_diff <= -" + min + " AND accel_x_diff >= -" + max + ")" +
                        "   OR (accel_y_diff >= " + min + " AND accel_y_diff <= " + max + ") OR (accel_y_diff <= -" + min + " AND accel_y_diff >= -" + max + ")" +
                        "   OR (accel_z_diff >= " + min + " AND accel_z_diff <= " + max + ") OR (accel_z_diff <= -" + min + " AND accel_z_diff >= -" + max + ")" +
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
                if (streetInfoRepository.findByLatitudeAndLongitude(coordinates.getLatitude(), coordinates.getLongitude()) == null) {
                    StreetInfoEntity entity = new StreetInfoEntity(severity, coordinates.getLatitude(), coordinates.getLongitude());
                    streetInfoRepository.save(entity);
                }
            }
        }
    }
}
