package Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionLogger {
    private StringBuilder logData;
    private StringBuilder headerBuilder;

    public ExecutionLogger() {
        this.logData = new StringBuilder();
        this.headerBuilder = new StringBuilder();
        buildHeader();
    }

    private void buildHeader() {
        headerBuilder.append("Fecha,Archivo,N(cantidad de individuos),G(generaciones),MutProb,CrossProb,");
        headerBuilder.append("Seleccion,Cruce,Mutacion,Supervivientes,");
        headerBuilder.append("FitnessInicial,FitnessFinal,Mejora,TiempoMs");
    }

    public void logExecution(String archivo, int N, int G, double mutProb, double crossProb,
                             String seleccion, String cruce, String mutacion, String supervivientes,
                             double fitnessInicial, double fitnessFinal, long tiempoMs,
                             Double porcentajeNN, Integer tamanioTorneo,
                             Integer numReemplazoSteadyState, Integer elite) {

        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        double mejora = ((fitnessFinal - fitnessInicial) / fitnessInicial) * 100;

        StringBuilder row = new StringBuilder();
        row.append(String.format("%s,%s,%d,%d,%.3f,%.3f,%s,%s,%s,%s,%.4f,%.4f,%.2f%%,%d",
                fecha, archivo, N, G, mutProb, crossProb, seleccion, cruce, mutacion,
                supervivientes, fitnessInicial, fitnessFinal, mejora, tiempoMs));

        if (porcentajeNN != null) row.append(",").append(porcentajeNN);
        if (tamanioTorneo != null) row.append(",").append(tamanioTorneo);
        if (numReemplazoSteadyState != null) row.append(",").append(numReemplazoSteadyState);
        if (elite != null) row.append(",").append(elite);

        row.append("\n");

        if (logData.length() == 0) {
            StringBuilder header = new StringBuilder(headerBuilder);
            if (porcentajeNN != null) header.append(",PorcentajeNN");
            if (tamanioTorneo != null) header.append(",TamanioTorneo");
            if (numReemplazoSteadyState != null) header.append(",NumReemplazoSteadyState");
            if (elite != null) header.append(",Elite");
            header.append("\n");
            logData.append(header);
        }

        logData.append(row);
    }

    public String toJson(String archivo, int N, int G, int C, double mutProb, double crossProb,
                         String seleccion, String cruce, String mutacion, String supervivientes,
                         double finalBestFitness, long executionTime,
                         List<Double> fitnessEvolution, ArrayList<Integer> bestSolution,
                         Double porcentajeNN, Integer tamanioTorneo,
                         Integer numReemplazoSteadyState, Integer elite, int bestCost) {

        StringBuilder json = new StringBuilder();
        json.append("{\n");

        // Fecha y archivo
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        json.append("  \"fecha\": \"").append(fecha).append("\",\n");
        json.append("  \"archivo\": \"").append(archivo).append("\",\n");

        // Configuración
        json.append("  \"configuracion\": {\n");
        json.append("    \"poblacion\": ").append(N).append(",\n");
        json.append("    \"generaciones\": ").append(G).append(",\n");
        json.append("    \"ciudades\": ").append(C).append(",\n");
        json.append("    \"probabilidadMutacion\": ").append(mutProb).append(",\n");
        json.append("    \"probabilidadCruce\": ").append(crossProb).append(",\n");

        if (porcentajeNN != null) {
            json.append("    \"porcentajeNN\": ").append(porcentajeNN).append(",\n");
        }

        json.append("    \"seleccion\": \"").append(seleccion).append("\"");
        if (tamanioTorneo != null) {
            json.append(",\n    \"tamanioTorneo\": ").append(tamanioTorneo);
        }
        json.append(",\n");

        json.append("    \"cruce\": \"").append(cruce).append("\",\n");
        json.append("    \"mutacion\": \"").append(mutacion).append("\",\n");

        json.append("    \"supervivientes\": \"").append(supervivientes).append("\"");
        if (numReemplazoSteadyState != null) {
            json.append(",\n    \"numReemplazoSteadyState\": ").append(numReemplazoSteadyState);
        }
        if (elite != null) {
            json.append(",\n    \"elite\": ").append(elite);
        }
        json.append("\n");

        json.append("  },\n");

        // Evolución del fitness
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
        json.append("  \"fitnessMejorSolucion\": ").append(finalBestFitness).append(",\n");

        //Costo de la mejor solución
        json.append("  \"costoMejorSolucion\": ").append(bestCost).append(",\n");

        // Tiempo requerido de ejecución
        json.append("  \"tiempoEjecucionMs\": ").append(executionTime).append("\n");

        json.append("}");

        return json.toString();
    }

    public void saveJsonToFile(String filename, String jsonContent) throws IOException {
        Files.write(Paths.get(filename), jsonContent.getBytes());
    }
}
