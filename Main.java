import java.io.File;
import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import Components.*;
import Model.*;
import Util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String filePath = null;

        // ========== SECCIÓN 1: CARGAR ARCHIVO ==========
        System.out.println("=== CONFIGURACIÓN DEL ALGORITMO GENÉTICO ===");
        System.out.println();
        System.out.print("Desea utilizar alguna de las instancias de testeo? (Ingrese S(si) o N(no)): ");
        String respuesta = scanner.nextLine();
        if (respuesta.equalsIgnoreCase("S")){
            System.out.println("Que archivo quiere usar? (ingrese 1 o 2): ");
            System.out.println(" (1) - br17.atsp (contiene 17 ciudades)");
            System.out.println(" (2) - p43.atsp (contiene 43 ciudades)");
            String archivo = scanner.nextLine();
            if (archivo.equals("1")){
                filePath = "Util/br17.atsp";
            }
            else if (archivo.equals("2")){
                filePath = "Util/p43.atsp";
            }
            else{
                System.out.println("Valor erróneo ingresado");
                scanner.close();
                return;
            }
        }
        else if (respuesta.equalsIgnoreCase("N")) {

            System.out.println("Por favor, ingrese la ruta a un archivo ATSP (sin comillas): ");
            filePath = scanner.nextLine();

            if (!filePath.endsWith(".atsp") || !new File(filePath).isFile()) {
                System.err.println("Archivo inválido o inexistente. Saliendo.");
                scanner.close();
                return;
            }
        }
        else {
            System.out.println("Valor erróneo ingresado");
            scanner.close();
            return;
        }

        System.out.println("Cargando archivo: " + filePath);
        File atspFile = new File(filePath);
        String fileName = atspFile.getName();
        int[][] costs = ATSPReader.read(atspFile);
        int C = costs.length;
        System.out.println("Número de ciudades a visitar: " + C);
        System.out.println();


        // ========== SECCIÓN 2: PARÁMETROS DEL ALGORITMO ==========
        System.out.println("--- PARÁMETROS DEL ALGORITMO ---");

        System.out.print("Número de individuos (ej. 10): ");
        int N = Integer.parseInt(scanner.nextLine());
        if (N <= 1) {
            System.err.println("El número de individuos debe ser mayor que 1. Saliendo.");
            scanner.close();
            return;
        }

        System.out.print("Número de generaciones (ej. 1000): ");
        int G = Integer.parseInt(scanner.nextLine());
        if (G <= 0) {
            System.err.println("El número de generaciones debe ser mayor que 0. Saliendo.");
            scanner.close();
            return;
        }

        System.out.print("Probabilidad de mutación (ej. 0.05): ");
        double mutProb = Double.parseDouble(scanner.nextLine());
        if (mutProb < 0.0 || mutProb > 1.0) {
            System.err.println("La probabilidad de mutación debe estar entre 0 y 1. Saliendo.");
            scanner.close();
            return;
        }

        System.out.print("Probabilidad de cruce (ej. 0.6): ");
        double crossProb = Double.parseDouble(scanner.nextLine());
        if (crossProb < 0.0 || crossProb > 1.0) {
            System.err.println("La probabilidad de cruce debe estar entre 0 y 1. Saliendo.");
            scanner.close();
            return;
        }
        System.out.println();


        // ========== SECCIÓN 3: MÉTODO DE INICIALIZACIÓN ==========
        System.out.println("--- MÉTODO DE INICIALIZACIÓN ---");
        System.out.print("Elija el método: 1) Aleatorio  2) Mixta con vecino más cercano (ingrese 1 o 2): ");
        String opcionInit = scanner.nextLine();

        double porcentajeNN = 0.0;
        if (opcionInit.equals("2")) {
            System.out.print("Ingrese el porcentaje de individuos con vecino más cercano (ej. 0.1): ");
            porcentajeNN = Double.parseDouble(scanner.nextLine());
            if (porcentajeNN < 0.0 || porcentajeNN > 1.0) {
                System.err.println("El porcentaje debe estar entre 0 y 1. Saliendo.");
                scanner.close();
                return;
            }
        } else if (!opcionInit.equals("1")) {
            System.err.println("Opción inválida. Saliendo.");
            scanner.close();
            return;
        }
        System.out.println();


        // ========== SECCIÓN 4: MÉTODO DE SELECCIÓN DE PADRES ==========
        System.out.println("--- MÉTODO DE SELECCIÓN DE PADRES ---");
        System.out.print("Elija el método: 1) Torneo  2) Rueda de Ruleta (ingrese 1 o 2): ");
        String opcionSeleccion = scanner.nextLine();
        Integer tamanioTorneo = null;

        FatherSelectionMethod selectionMethod;
        if (opcionSeleccion.equals("1")) {
            System.out.print("Tamaño del torneo (se sugiere valores entre 2 y 10): ");
            tamanioTorneo = Integer.parseInt(scanner.nextLine());
            if (tamanioTorneo <= 1 || tamanioTorneo >= N) {
                System.err.println("El tamaño del torneo no es válido. Saliendo.");
                scanner.close();
                return;
            }
            selectionMethod = new TournamentMethod(tamanioTorneo);
        } else if (opcionSeleccion.equals("2")) {
            selectionMethod = new RouletteWheelMethod();
        } else {
            System.err.println("Opción inválida. Saliendo.");
            scanner.close();
            return;
        }
        System.out.println();


        // ========== SECCIÓN 5: MÉTODO DE CRUCE ==========
        System.out.println("--- MÉTODO DE CRUCE ---");
        System.out.print("Elija el método: 1) PMX (Partially Mapped Crossover) 2) Cruce Basado en Arcos (ingrese 1 o 2): ");
        String opcionCruce = scanner.nextLine();

        CrossMethod crossMethod;
        if (opcionCruce.equals("1")) {
            crossMethod = new PMXCrossMethod();
        } else if (opcionCruce.equals("2")) {
            crossMethod = new ArchesBasedCrossingMethod();
        } else {
            System.err.println("Opción inválida. Saliendo.");
            scanner.close();
            return;
        }


        // ========== SECCIÓN 6: MÉTODO DE MUTACIÓN ==========
        System.out.println("--- MÉTODO DE MUTACIÓN ---");
        System.out.print("Elija el método: 1) Mutación por Inversión 2) Mutación por desplazamiento (ingrese 1 o 2): ");
        String opcionMutacion = scanner.nextLine();

        MutationMethod mutationMethod;
        if (opcionMutacion.equals("1")) {
            mutationMethod = new InvertMutationMethod();
        } else if (opcionMutacion.equals("2")) {
            mutationMethod = new ShiftMutationMethod();
        } else {
            System.err.println("Opción inválida. Saliendo.");
            scanner.close();
            return;
        }
        System.out.println(" ");


        // ========== SECCIÓN 7: MÉTODO DE SELECCIÓN DE SUPERVIVIENTES ==========
        System.out.println("-- MÉTODO DE SELECCIÓN DE SUPERVIVIENTES ---");
        System.out.print("Elija el método: 1) Steady-State  2) Elitismo (ingrese 1 o 2): ");
        String opcionSobrevientes = scanner.nextLine();
        Integer numReemplazoSteadyState = null;
        Integer elite = null;

        SurvivorsSelectionMethod survivorMethod;
        if (opcionSobrevientes.equals("1")) {
            System.out.println("Seleccione el valor de n (cantidad de individuos de la anterior generación a ser reemplazados en la siguiente): ");
            numReemplazoSteadyState = Integer.parseInt(scanner.nextLine());
            if (numReemplazoSteadyState >= N || numReemplazoSteadyState <= 0) {
                System.out.println("El valor a reemplazar debe ser menor al tamaño de la población y mayor a 0");
                scanner.close();
                return;
            }
            survivorMethod = new SteadyStateSurvivorSelectionMethod(numReemplazoSteadyState);
        } else if (opcionSobrevientes.equals("2")) {
            System.out.println("Seleccione el valor de k (cantidad de mejores individuos de la generación a preservar en la siguiente): ");
            elite = Integer.parseInt(scanner.nextLine());
            if (elite < 0 || elite > N) {
                System.out.println("El valor ingresado no es válido. Debe ser superior o igual a 0 y menor al tamaño de la población");
                scanner.close();
                return;
            }
            survivorMethod = new ElitismSurvivorMethod(elite);
        } else {
            System.err.println("Opción inválida. Saliendo.");
            scanner.close();
            return;
        }

        System.out.println("Guardando configuración..");
        //Guardado de la configuración
        ExecutionConfig configuracion = new ExecutionConfig(fileName, N, G, C, mutProb, crossProb, selectionMethod.getName(),
                crossMethod.getName(), mutationMethod.getName(), survivorMethod.getName(), porcentajeNN, tamanioTorneo,
                numReemplazoSteadyState, elite);

        ExecutionLogger logger = new ExecutionLogger();


        // ========== EJECUCIÓN DEL ALGORITMO ==========
        System.out.println("  ");
        System.out.println("========================================");
        System.out.println("INICIANDO ALGORITMO GENÉTICO");
        System.out.println("========================================");
        System.out.println();

        // Inicializar población
        Population p = Population.createMixedPopulation(N, C, costs, mutProb, crossProb, G, porcentajeNN);

        System.out.println("--- POBLACIÓN INICIAL ---");
        p.printPaths();
        System.out.println("Mejor camino inicial: " + p.getBestPath());
        System.out.println();

        // Crear instancia de métricas
        EvolutionMetrics metricas = new EvolutionMetrics();
        Random random = new Random();
        metricas.startTracking();

        // Bucle de generaciones
        for (int generacion = 0; generacion < G; generacion++) {
            ArrayList<Path> generatedPaths = p.getPaths();

            // 1. Seleccionar padres y formar parejas
            ArrayList<Couple> parejas = generateCouple(generatedPaths, N, selectionMethod);

            // 2. Generar hijos con cruce y mutación
            ArrayList<Path> nuevaGeneracion = new ArrayList<>();

            for (Couple pareja : parejas) {
                ArrayList<Path> hijos;

                // Decidir si aplicar cruce
                if (random.nextDouble() < crossProb) {
                    hijos = crossMethod.crossCouple(pareja);
                } else {
                    // Sin cruce, hijos clones de los padres
                    Path hijo1 = new Path(pareja.getPadre1().getCities(), pareja.getPadre1().getCosts());
                    Path hijo2 = new Path(pareja.getPadre2().getCities(), pareja.getPadre2().getCosts());
                    hijos = new ArrayList<>();
                    hijos.add(hijo1);
                    hijos.add(hijo2);
                }

                // Decidir si aplicar mutación a cada hijo
                for (Path hijo : hijos) {
                    if (random.nextDouble() < mutProb) {
                        mutationMethod.mutate(hijo);
                    }
                }

                nuevaGeneracion.addAll(hijos);
            }

            // 3. Reemplazar población y armar la población para la nueva generación
            nuevaGeneracion = survivorMethod.selectSurvivors(p.getPaths(), nuevaGeneracion);
            p.setPaths(nuevaGeneracion);

            // 4. Registrar métricas de evolución
            metricas.recordGeneration(generacion, p);

        }

        metricas.stopTracking(System.currentTimeMillis());

        System.out.println("Guardando resultados de la ejecución...");
        ExecutionResult resultadosEjecucion = new ExecutionResult(metricas.getFinalBestFitness(), p.getBestPath().getPathCost(),
                metricas.getTiempoEjecucion(), metricas.getBestFitnessList(), p.getBestPath().getCities());



        System.out.println();
        System.out.println("========================================");
        System.out.println("RESULTADO FINAL");

        try {
            // Crear carpeta para la ejecución
            File carpetaEjecucion = crearCarpetaEjecucion();
            System.out.println("Resultados almacenados en la carpeta: " + carpetaEjecucion.getPath());

            // Guardar en JSON
            String json = logger.toJson(configuracion, resultadosEjecucion);

            File jsonFile = new File(carpetaEjecucion, "ejecucion.json");
            logger.saveJsonToFile(jsonFile.getPath(), json);

            // Generar HTML con las estadísticas de la ejecución
            File htmlFile = new File(carpetaEjecucion, "evolucion.html");
            EvolutionChart.generateHTML(metricas, htmlFile.getPath(), new File(filePath).getName());

            System.out.println("Reporte generado en: " + carpetaEjecucion.getPath());
            System.out.println("  - " + jsonFile.getName());
            System.out.println("  - " + htmlFile.getName());
        } catch (IOException e) {
            System.err.println("Error al generar reporte de los resultados: " + e.getMessage());
        }


        scanner.close();

    }

    private static ArrayList<Couple> generateCouple(ArrayList<Path> generatedPaths, int N, FatherSelectionMethod strategy) {
        ArrayList<Couple> parejas = new ArrayList<>();
        for (int i = 0; i < N / 2; i++) {
            Path padre1 = strategy.selectFather(generatedPaths);
            Path padre2 = strategy.selectFather(generatedPaths);
            while (padre1.equals(padre2)) {
                padre2 = strategy.selectFather(generatedPaths);
            }
            Couple parejaGenerada = new Couple(padre1, padre2);
            parejas.add(parejaGenerada);
        }
        return parejas;
    }

    //Crea las carpetas para almacenar los resultados de cada ejecución (.json + .html)
    private static File crearCarpetaEjecucion() {
        File testsDir = new File("Tests");
        if (!testsDir.exists()) {
            testsDir.mkdirs();
        }

        int numeroEjecucion = 1;
        File carpetaEjecucion;

        // Buscar el siguiente número de ejecución disponible
        do {
            carpetaEjecucion = new File(testsDir, "ejecucion" + numeroEjecucion);
            numeroEjecucion++;
        } while (carpetaEjecucion.exists());

        // Crear la carpeta
        carpetaEjecucion.mkdirs();

        return carpetaEjecucion;
    }

}
