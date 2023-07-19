package challange.api.unit;

import challange.api.SensorData;
import challange.api.SensorDataService;
import challange.api.SensorDataController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Validated
public class SensorDataControllerTest {

    private SensorDataController controller;
    private SensorDataService service;

    @BeforeEach
    public void setup() {
        service = mock(SensorDataService.class);
        controller = new SensorDataController(service);
    }

    @Test
    public void addSensorData_ValidData_ReturnsOkResponse() {
        SensorData sensorData = new SensorData(1, "temperature", 24.0, LocalDateTime.now());

        when(service.addSensorData(Mockito.any(SensorData.class))).thenReturn(sensorData);

        ResponseEntity<SensorData> response = controller.addSensorData(sensorData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sensorData, response.getBody());
    }

    @Test
    public void getSensorData_ValidData_ReturnsOkResponse() {
        Map<String, Double> expectedResults = new HashMap<>();
        expectedResults.put("temperature", 25.0);

        when(service.calculateStatistics(anyList(), anyList(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedResults);

        ResponseEntity<Map<String, Double>> response = controller.getSensorData("1", "temperature", "avg", "2023-07-15T13:00:00.000", "2023-07-18T13:00:00.000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
    }
}
