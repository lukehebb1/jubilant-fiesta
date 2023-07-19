package challange.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensor")
public class SensorDataController {

    private final SensorDataService service;

    public SensorDataController(SensorDataService service) {
        this.service = service;
    }

    @PostMapping("/data")
    public ResponseEntity<SensorData> addSensorData(@Validated @RequestBody SensorData sensorData) {
        LocalDateTime dateTime = sensorData.getDateTime();
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
            sensorData.setDateTime(dateTime);
        }

        return ResponseEntity.ok(service.addSensorData(sensorData));
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, Double>> getSensorData(@RequestParam String sensorId, @RequestParam String metric,
                                                             @RequestParam String statistic, @RequestParam(required = false) String from,
                                                             @RequestParam(required = false) String to) {
        List<Integer> sensorIds = Arrays.stream(sensorId.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<String> metricTypes = Arrays.stream(metric.split(",")).map(String::strip).collect(Collectors.toList());

        LocalDateTime fromDateTime;
        LocalDateTime toDateTime;

        if (from.isEmpty() || to.isEmpty()) {
            // If the range is not provided, default to the latest data (from one day ago to now)
            toDateTime = LocalDateTime.now();
            fromDateTime = toDateTime.minusDays(1);
        } else {
            // Parse the dates
            fromDateTime = LocalDateTime.parse(from, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            toDateTime = LocalDateTime.parse(to, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        Map<String, Double> results = service.calculateStatistics(sensorIds, metricTypes, statistic, fromDateTime, toDateTime);
        return ResponseEntity.ok(results);
    }
}
