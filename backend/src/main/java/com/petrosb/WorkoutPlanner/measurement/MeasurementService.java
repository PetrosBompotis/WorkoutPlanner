package com.petrosb.WorkoutPlanner.measurement;

import com.petrosb.WorkoutPlanner.customer.Customer;
import com.petrosb.WorkoutPlanner.customer.CustomerDataAccessService;
import com.petrosb.WorkoutPlanner.exception.RequestValidationException;
import com.petrosb.WorkoutPlanner.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementService {
    private final MeasurementDataAccessService measurementDataAccessService;
    private final CustomerDataAccessService customerDataAccessService;

    public MeasurementService(MeasurementDataAccessService measurementDataAccessService, CustomerDataAccessService customerDataAccessService) {
        this.measurementDataAccessService = measurementDataAccessService;
        this.customerDataAccessService = customerDataAccessService;
    }

    public List<Measurement> getAllMeasurementsByCustomerId(Long customerId){
        if(!customerDataAccessService.existsPersonWithId(customerId)){
            throw new ResourceNotFoundException("Customer with id [%s] not found".formatted(customerId));
        }
        return measurementDataAccessService.selectAllMeasurementsByCustomerId(customerId);
    }

    public void addMeasurement(MeasurementCreationRequest measurementCreationRequest, Long customerId){
        Customer customer = customerDataAccessService.selectCustomerByID(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id [%s] not found".formatted(customerId)
                ));

        Measurement measurement = new Measurement(
                measurementCreationRequest.bodyFatPercentage(),
                measurementCreationRequest.bodyWeight(),
                measurementCreationRequest.createdAt(),
                customer
        );

        measurementDataAccessService.insertMeasurement(measurement);
    }

    public void deleteMeasurementById(Long measurementId){
        //check if id exists
        if(!measurementDataAccessService.existsMeasurementWithId(measurementId)){
            throw new ResourceNotFoundException("Workout plan with id [%s] not found".formatted(measurementId));
        }

        //otherwise remove
        measurementDataAccessService.deleteMeasurementById(measurementId);

    }

    public void updateMeasurement(MeasurementUpdateRequest updateRequest, Long measurementId){

        Measurement measurement = measurementDataAccessService.selectMeasurementByID(measurementId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan with id [%s] not found".formatted(measurementId)
                ));
        boolean changes = false;
        //check if attributes need change exists
        if (updateRequest.bodyFatPercentage() != null && !updateRequest.bodyFatPercentage().equals(measurement.getBodyFatPercentage())){
            measurement.setBodyFatPercentage(updateRequest.bodyFatPercentage());
            changes = true;
        }

        if (updateRequest.bodyWeight() != null && !updateRequest.bodyWeight().equals(measurement.getBodyWeight())){
            measurement.setBodyWeight(updateRequest.bodyWeight());
            changes = true;
        }

        //otherwise update
        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        measurementDataAccessService.updateMeasurementById(measurement);
    }
}
