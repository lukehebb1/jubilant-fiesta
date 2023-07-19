package challange.api.unit;

import challange.api.SensorData;
import challange.api.SensorDataRepository;
import challange.api.SensorDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorDataServiceTest {

    @Mock
    private SensorDataRepository repository;

    @InjectMocks
    private SensorDataService service;

    @Test
    public void getSensorData_ValidData_ReturnsSensorDataList() {
        // Mock data
        SensorData sensorData1 = new SensorData(1, "temperature", 24.0, LocalDateTime.now());
        SensorData sensorData2 = new SensorData(1, "temperature", 24.0, LocalDateTime.now());;
        List<SensorData> expectedData = Arrays.asList(sensorData1, sensorData2);

        // Mock repository behavior
        when(repository.findSensorData(Mockito.anyList(), Mockito.anyList(), Mockito.any(), Mockito.any()))
                .thenReturn(expectedData);

        // Perform the test
        List<SensorData> result = service.getSensorData(Arrays.asList(1, 2), Arrays.asList("temperature", "humidity"),
                LocalDateTime.now().minusDays(1), LocalDateTime.now());

        // Verify the result
        assertEquals(expectedData, result);
        verify(repository).findSensorData(Mockito.anyList(), Mockito.anyList(), Mockito.any(), Mockito.any());
    }

    @Test
    public void addSensorData_ValidData_ReturnsSavedSensorData() {
        // Mock data
        SensorData sensorData = new SensorData(1, "temperature", 24.0, LocalDateTime.now());

        // Mock repository behavior
        when(repository.save(any(SensorData.class))).thenReturn(sensorData);

        // Perform the test
        SensorData result = service.addSensorData(sensorData);

        // Verify the result
        assertEquals(sensorData, result);
        verify(repository).save(any(SensorData.class));
    }

    @Test
    void calculateStatistics_ValidParameters_ReturnsStatisticsMap() {
        // Arrange
        List<Integer> sensorIds = Arrays.asList(1, 2, 3);
        List<String> metricTypes = Arrays.asList("temperature", "humidity");
        String statistic = "avg";
        LocalDateTime from = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2022, 1, 31, 23, 59);
        List<SensorData> sensorData = Arrays.asList(
                new SensorData(1, "temperature", 24.0, LocalDateTime.now()),
                new SensorData(2, "temperature", 25.0, LocalDateTime.now()),
                new SensorData(3, "humidity", 50.0, LocalDateTime.now())
        );
        when(repository.findSensorData(sensorIds, metricTypes, from, to)).thenReturn(sensorData);

        // Act
        Map<String, Double> result = service.calculateStatistics(sensorIds, metricTypes, statistic, from, to);

        // Assert
        assertNotNull(result);
        assertTrue(((Map<?, ?>) result).containsKey("temperature"));
        assertTrue(result.containsKey("humidity"));
        assertEquals(24.5, result.get("temperature"));
        assertEquals(50.0, result.get("humidity"));
        verify(repository, times(2)).findSensorData(sensorIds, metricTypes, from, to);
    }
}
