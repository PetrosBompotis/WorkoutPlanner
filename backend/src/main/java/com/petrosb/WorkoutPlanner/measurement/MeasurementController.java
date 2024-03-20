package com.petrosb.WorkoutPlanner.measurement;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class MeasurementController {
    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping("/customers/{customerId}/measurements")
    public List<Measurement> getAllMeasurementsByCustomerId(@PathVariable(value = "customerId") Long customerId) {
        return measurementService.getAllMeasurementsByCustomerId(customerId);
    }

    @PostMapping("/customers/{customerId}/measurements")
    public void createMeasurement(@PathVariable(value = "customerId") Long customerId,
                                  @RequestBody MeasurementCreationRequest measurementCreationRequest) {
        measurementService.addMeasurement(measurementCreationRequest, customerId);
    }

    @DeleteMapping("/measurements/{measurementId}")
    public void deleteMeasurements(@PathVariable("measurementId") Long measurementId) {
        measurementService.deleteMeasurementById(measurementId);
    }

    @PutMapping("/measurements/{measurementId}")
    public void updateMeasurement(@RequestBody MeasurementUpdateRequest updateRequest,
                                  @PathVariable("measurementId") Long measurementId) {
        measurementService.updateMeasurement(updateRequest, measurementId);
    }
}
