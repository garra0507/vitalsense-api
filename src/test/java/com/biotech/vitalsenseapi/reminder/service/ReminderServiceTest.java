package com.biotech.vitalsenseapi.reminder.service;

import com.biotech.vitalsenseapi.patient.model.Patient;
import com.biotech.vitalsenseapi.patient.repository.PatientRepository;
import com.biotech.vitalsenseapi.reminder.dto.ReminderRequestDTO;
import com.biotech.vitalsenseapi.reminder.dto.ReminderResponseDTO;
import com.biotech.vitalsenseapi.reminder.mapper.ReminderMapper;
import com.biotech.vitalsenseapi.reminder.model.Reminder;
import com.biotech.vitalsenseapi.reminder.repository.ReminderRepository;
import com.biotech.vitalsenseapi.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceTest {

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ReminderMapper reminderMapper;

    @InjectMocks
    private ReminderService reminderService;

    private Patient mockPatient;
    private Reminder mockReminder;
    private ReminderRequestDTO mockRequestDTO;
    private ReminderResponseDTO mockResponseDTO;

    @BeforeEach
    void setUp() {
        mockPatient = new Patient();
        mockPatient.setPatientId(1L);

        mockReminder = Reminder.builder()
                .reminderId(10L)
                .patient(mockPatient)
                .medicationName("Paracetamol")
                .purpose("Fiebre")
                .frequency("L,M,X")
                .reminderTime(LocalTime.of(8, 0))
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        mockRequestDTO = new ReminderRequestDTO();
        mockRequestDTO.setPatientId(1L);
        mockRequestDTO.setMedicationName("Paracetamol");
        mockRequestDTO.setPurpose("Fiebre");
        mockRequestDTO.setFrequency("L,M,X");
        mockRequestDTO.setReminderTime(LocalTime.of(8, 0));

        mockResponseDTO = ReminderResponseDTO.builder()
                .reminderId(10L)
                .patientId(1L)
                .medicationName("Paracetamol")
                .purpose("Fiebre")
                .frequency("L,M,X")
                .reminderTime(LocalTime.of(8, 0))
                .active(true)
                .build();
    }

    @Test
    void createReminder_Success() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.of(mockPatient));
        when(reminderRepository.save(any(Reminder.class))).thenReturn(mockReminder);
        when(reminderMapper.toResponseDTO(mockReminder)).thenReturn(mockResponseDTO);

        // Act
        ReminderResponseDTO result = reminderService.createReminder(mockRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Paracetamol", result.getMedicationName());
        assertTrue(result.getActive());
        verify(patientRepository, times(1)).findById(1L);
        verify(reminderRepository, times(1)).save(any(Reminder.class));
    }

    @Test
    void createReminder_PatientNotFound_ThrowsException() {
        // Arrange
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            reminderService.createReminder(mockRequestDTO);
        });

        assertEquals("Patient not found", exception.getMessage());
        verify(reminderRepository, never()).save(any());
    }

    @Test
    void getRemindersByPatient_Success() {
        // Arrange
        when(reminderRepository.findByPatientPatientId(1L)).thenReturn(List.of(mockReminder));
        when(reminderMapper.toResponseDTO(mockReminder)).thenReturn(mockResponseDTO);

        // Act
        List<ReminderResponseDTO> result = reminderService.getRemindersByPatient(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getMedicationName());
        verify(reminderRepository, times(1)).findByPatientPatientId(1L);
    }

    @Test
    void toggleReminder_Success() {
        // Arrange
        when(reminderRepository.findById(10L)).thenReturn(Optional.of(mockReminder));
        when(reminderRepository.save(any(Reminder.class))).thenReturn(mockReminder);
        
        ReminderResponseDTO inactiveResponse = ReminderResponseDTO.builder()
                .reminderId(10L)
                .active(false)
                .build();
        when(reminderMapper.toResponseDTO(mockReminder)).thenReturn(inactiveResponse);

        // Act
        ReminderResponseDTO result = reminderService.toggleReminder(10L);

        // Assert
        assertNotNull(result);
        assertFalse(result.getActive()); // Verify the state was flipped
        verify(reminderRepository, times(1)).findById(10L);
        verify(reminderRepository, times(1)).save(mockReminder);
    }

    @Test
    void deleteReminder_Success() {
        // Arrange
        when(reminderRepository.existsById(10L)).thenReturn(true);
        doNothing().when(reminderRepository).deleteById(10L);

        // Act
        assertDoesNotThrow(() -> reminderService.deleteReminder(10L));

        // Assert
        verify(reminderRepository, times(1)).existsById(10L);
        verify(reminderRepository, times(1)).deleteById(10L);
    }

    @Test
    void deleteReminder_NotFound_ThrowsException() {
        // Arrange
        when(reminderRepository.existsById(10L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            reminderService.deleteReminder(10L);
        });

        verify(reminderRepository, never()).deleteById(anyLong());
    }
}
