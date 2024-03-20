package com.petrosb.WorkoutPlanner.measurement;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MeasurementDataAccessService {
    private final MeasurementRepository measurementRepository;

    public MeasurementDataAccessService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public List<Measurement> selectAllMeasurementsByCustomerId(Long customerId){
        return measurementRepository.findByCustomerId(customerId);
    }

    public Optional<Measurement> selectMeasurementByID(Long id) {
        return measurementRepository.findById(id);
    }

    public void insertMeasurement(Measurement measurement) {
        measurementRepository.save(measurement);
    }

    public boolean existsMeasurementWithId(Long id) {
        return measurementRepository.existsMeasurementById(id);
    }

    public void deleteMeasurementById(Long measurementId) {
        measurementRepository.deleteById(measurementId);
    }

    public void updateMeasurementById(Measurement measurement) {
        measurementRepository.save(measurement);
    }
}
