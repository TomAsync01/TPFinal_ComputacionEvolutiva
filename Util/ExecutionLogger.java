package Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExecutionLogger {

    public String toJson(ExecutionConfig configuracion, ExecutionResult resultadosEjecucion) {

        StringBuilder json = new StringBuilder();
        json.append("{\n");

        // Fecha y archivo
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        json.append("  \"fecha\": \"").append(fecha).append("\",\n");
        json.append("  \"archivo\": \"").append(configuracion.getArchivo()).append("\",\n");

        // Configuración
        json.append("  \"configuracion\": {\n");
        json.append("    \"poblacion\": ").append(configuracion.getN()).append(",\n");
        json.append("    \"generaciones\": ").append(configuracion.getG()).append(",\n");
        json.append("    \"ciudades\": ").append(configuracion.getC()).append(",\n");
        json.append("    \"probabilidadMutacion\": ").append(configuracion.getMutProb()).append(",\n");
        json.append("    \"probabilidadCruce\": ").append(configuracion.getCrossProb()).append(",\n");

        Double porcentajeNN = configuracion.getPorcentajeNN();
        Integer tamanioTorneo = configuracion.getTamanioTorneo();
        Integer numReemplazoSteadyState = configuracion.getNumReemplazoSteadyState();
        Integer elite = configuracion.getElite();


        if (porcentajeNN != null) {
            json.append("    \"porcentajeNN\": ").append(porcentajeNN).append(",\n");
        }

        json.append("    \"seleccion\": \"").append(configuracion.getSeleccion()).append("\"");
        if (tamanioTorneo != null) {
            json.append(",\n    \"tamanioTorneo\": ").append(tamanioTorneo);
        }
        json.append(",\n");

        json.append("    \"cruce\": \"").append(configuracion.getCruce()).append("\",\n");
        json.append("    \"mutacion\": \"").append(configuracion.getMutacion()).append("\",\n");

        json.append("    \"supervivientes\": \"").append(configuracion.getSupervivientes()).append("\"");
        if (numReemplazoSteadyState != null) {
            json.append(",\n    \"numReemplazoSteadyState\": ").append(numReemplazoSteadyState);
        }
        if (elite != null) {
            json.append(",\n    \"elite\": ").append(elite);
        }
        json.append("\n");

        json.append("  },\n");

        List<Double> fitnessEvolution = resultadosEjecucion.getFitnessEvolution();
        List<Integer> bestSolution = resultadosEjecucion.getBestSolution();

        // Evolución del mejro fitness para cada generación
        json.append("  \"evolucionFitness\": [");
        for (int i = 0; i < fitnessEvolution.size(); i++) {
            json.append("\n    {\"generacion\": ").append(i)
                    .append(", \"fitness\": ").append(fitnessEvolution.get(i)).append("}");
            if (i < fitnessEvolution.size() - 1) json.append(",");
        }
        json.append("\n  ],\n");

        // Mejor solución
        json.append("  \"mejorSolucion\": [");
        for (int i = 0; i < bestSolution.size(); i++) {
            json.append(bestSolution.get(i));
            if (i < bestSolution.size() - 1) json.append(", ");
        }
        json.append("],\n");

        // Fitness de la mejor solución
        json.append("  \"fitnessMejorSolucion\": ").append(resultadosEjecucion.getFinalBestFitness()).append(",\n");

        //Costo de la mejor solución
        json.append("  \"costoMejorSolucion\": ").append(resultadosEjecucion.getBestCost()).append(",\n");

        // Tiempo requerido de ejecución
        json.append("  \"tiempoEjecucionMs\": ").append(resultadosEjecucion.getExecutionTime()).append("\n");

        json.append("}");

        return json.toString();
    }

    public void saveJsonToFile(String filename, String jsonContent) throws IOException {
        Files.write(Paths.get(filename), jsonContent.getBytes());
    }
}
