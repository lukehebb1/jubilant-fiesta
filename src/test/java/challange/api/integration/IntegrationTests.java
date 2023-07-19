package challange.api.integration;

import challange.api.SensorData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {
    /**
     * Integration tests. Not currently working, wanted to include them to show my work.
     */

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addSensorData() {
        // Prepare the request body
        SensorData sensorData = new SensorData(1, "temperature", 25.0, LocalDateTime.now());

        // Make a POST request to add sensor data
        ResponseEntity<SensorData> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/sensor/data",
                sensorData,
                SensorData.class
        );

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        SensorData addedSensorData = response.getBody();
        assertEquals(sensorData.getSensorId(), addedSensorData.getSensorId());
        assertEquals(sensorData.getMetricType(), addedSensorData.getMetricType());
        assertEquals(sensorData.getMetricValue(), addedSensorData.getMetricValue());
        assertNotNull(addedSensorData.getDateTime());
    }

    @Test
    public void getSensorData() {
        // Prepare the query parameters
        String sensorId = "1,2";
        String metric = "temperature";
        String statistic = "avg";

        // Make a GET request to retrieve sensor data
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/sensor/data?sensorId={sensorId}&metric={metric}&statistic={statistic}",
                Map.class,
                sensorId,
                metric,
                statistic
        );

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Double> sensorDataMap = response.getBody();
        List<String> metricTypes = Arrays.asList(metric.split(","));
        assertEquals(metricTypes.size(), sensorDataMap.size());

        for (String metricType : metricTypes) {
            assertTrue(sensorDataMap.containsKey(metricType));
            assertNotNull(sensorDataMap.get(metricType));
        }
    }
}
