package challange.api;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataRepository extends MongoRepository<SensorData, String> {

    @Query("{ 'sensorId': { $in: ?0 }, 'metricType': { $in: ?1 }, 'dateTime': { $gte: ?2, $lte: ?3 } }")
    List<SensorData> findSensorData(List<Integer> sensorIds, List<String> metricTypes, LocalDateTime from, LocalDateTime to);
}
