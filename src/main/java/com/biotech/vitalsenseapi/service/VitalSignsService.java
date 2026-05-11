package com.biotech.vitalsenseapi.service;

import org.springframework.stereotype.Service;

@Service
public class VitalSignsService {

    // US08 - Registro de Signos
    public void registerVitals(double temp, int heartRate) {
        if (temp > 35 && temp < 42) {
            System.out.println("Signos vitales registrados correctamente.");
        }
    }

    // US09 - Consulta de Historial
    public String getHistory() {
        return "Historial de signos vitales: Estable";
    }
}