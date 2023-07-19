package challange.api;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    private final SensorDataRepository repository;

    public SensorDataService(SensorDataRepository repository) {
        this.repository = repository;
    }

    public SensorData addSensorData(SensorData sensorData) {
        return repository.save(sensorData);
    }

    public List<SensorData> getSensorData(List<Integer> sensorNames, List<String> metricTypes, LocalDateTime from, LocalDateTime to) {
        return repository.findSensorData(sensorNames, metricTypes, from, to);
    }
    public Map<String, Double> calculateStatistics(List<Integer> sensorIds, List<String> metricTypes, String statistic, LocalDateTime from, LocalDateTime to) {
        return metricTypes.stream().collect(Collectors.toMap(
                metricType -> metricType,
                metricType -> {
                    List<SensorData> sensorData = repository.findSensorData(sensorIds, metricTypes, from, to);
                    List<Double> values = sensorData.stream()
                            .filter(data -> data.getMetricType().equals(metricType))
                            .map(SensorData::getMetricValue)
                            .collect(Collectors.toList());

                    switch (statistic.toLowerCase()) {
                        case "min":
                            return values.stream().mapToDouble(v -> v).min().orElse(Double.NaN);
                        case "max":
                            return values.stream().mapToDouble(v -> v).max().orElse(Double.NaN);
                        case "avg":
                            return values.stream().mapToDouble(v -> v).average().orElse(Double.NaN);
                        case "sum":
                            return values.stream().mapToDouble(v -> v).sum();
                        default:
                            throw new IllegalArgumentException("Invalid statistic: " + statistic);
                    }
                }));
    }
}
