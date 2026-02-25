package Util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EvolutionChart {

    // Establece los estilos de los gráficos y los devuelve
    protected static String getStyles() {
        StringBuilder styles = new StringBuilder();
        styles.append("    <style>\n");
        styles.append("        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n");
        styles.append("        .container { max-width: 1400px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
        styles.append("        h1 { color: #333; text-align: center; margin-bottom: 30px; }\n");
        styles.append("        .chart-container { position: relative; height: 500px; margin-bottom: 40px; }\n");
        styles.append("        .stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-top: 30px; }\n");
        styles.append("        .stat-card { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 20px; border-radius: 8px; color: white; }\n");
        styles.append("        .stat-label { font-size: 14px; opacity: 0.9; margin-bottom: 5px; }\n");
        styles.append("        .stat-value { font-size: 24px; font-weight: bold; }\n");
        styles.append("        @keyframes fadeIn { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }\n");
        styles.append("        .chart-container { animation: fadeIn 0.6s ease-out; }\n");
        styles.append("        .stat-card { animation: fadeIn 0.6s ease-out; transition: transform 0.3s; }\n");
        styles.append("        .stat-card:hover { transform: translateY(-5px); box-shadow: 0 5px 20px rgba(0,0,0,0.2); }\n");
        styles.append("    </style>\n");
        return styles.toString();
    }

    // Genera el script para los gráficos de cada una de las ejecuciones
    public static String generateChartScript(EvolutionMetrics metrics, String idSuffix) {
        StringBuilder script = new StringBuilder();

        script.append("const generations").append(idSuffix).append(" = ").append(metrics.getGenerationList()).append(";\n");
        script.append("const bestFitness").append(idSuffix).append(" = ").append(formatList(metrics.getBestFitnessList())).append(";\n");
        script.append("const diversity").append(idSuffix).append(" = ").append(formatList(metrics.getDiversityList())).append(";\n\n");

        // Gráfico de Mejor Fitness
        script.append("new Chart(document.getElementById('fitnessChart").append(idSuffix).append("'), {\n");
        script.append("    type: 'line',\n");
        script.append("    data: {\n");
        script.append("        labels: generations").append(idSuffix).append(",\n");
        script.append("        datasets: [\n");
        script.append("            {\n");
        script.append("                label: 'Mejor Fitness',\n");
        script.append("                data: bestFitness").append(idSuffix).append(",\n");
        script.append("                borderColor: 'rgb(75, 192, 192)',\n");
        script.append("                backgroundColor: 'rgba(75, 192, 192, 0.2)',\n");
        script.append("                borderWidth: 3,\n");
        script.append("                fill: false,\n");
        script.append("                tension: 0.4,\n");
        script.append("                pointRadius: 0,\n");
        script.append("            }\n");
        script.append("        ]\n");
        script.append("    },\n");
        script.append("    options: {\n");
        script.append("        responsive: true,\n");
        script.append("        maintainAspectRatio: false,\n");
        script.append("        interaction: { mode: 'index', intersect: false },\n");
        script.append("        plugins: {\n");
        script.append("            title: {\n");
        script.append("                display: true,\n");
        script.append("                text: 'Evolución del Mejor Fitness',\n");
        script.append("                font: { size: 18, weight: 'bold' }\n");
        script.append("            },\n");
        script.append("            legend: { display: false },\n");
        script.append("            tooltip: {\n");
        script.append("                callbacks: {\n");
        script.append("                    label: function(context) {\n");
        script.append("                        return 'Fitness: ' + context.parsed.y.toExponential(4);\n");
        script.append("                    }\n");
        script.append("                }\n");
        script.append("            }\n");
        script.append("        },\n");
        script.append("        scales: {\n");
        script.append("            x: {\n");
        script.append("                title: { display: true, text: 'Generación', font: { size: 14 } }\n");
        script.append("            },\n");
        script.append("            y: {\n");
        script.append("                title: { display: true, text: 'Fitness', font: { size: 14 } },\n");
        script.append("                ticks: {\n");
        script.append("                    callback: function(value) {\n");
        script.append("                        return value.toExponential(2);\n");
        script.append("                    }\n");
        script.append("                }\n");
        script.append("            }\n");
        script.append("        }\n");
        script.append("    }\n");
        script.append("});\n\n");

        // Gráfico de Diversidad
        script.append("const avgDiversity").append(idSuffix).append(" = ").append(metrics.getAverageDiversity()).append(";\n");
        script.append("const avgLine").append(idSuffix).append(" = Array(diversity").append(idSuffix).append(".length).fill(avgDiversity").append(idSuffix).append(");\n\n");
        script.append("new Chart(document.getElementById('diversityChart").append(idSuffix).append("'), {\n");
        script.append("    type: 'line',\n");
        script.append("    data: {\n");
        script.append("        labels: generations").append(idSuffix).append(",\n");
        script.append("        datasets: [\n");
        script.append("            {\n");
        script.append("                label: 'Diversidad',\n");
        script.append("                data: diversity").append(idSuffix).append(",\n");
        script.append("                borderColor: 'rgb(153, 102, 255)',\n");
        script.append("                backgroundColor: 'rgba(153, 102, 255, 0.1)',\n");
        script.append("                borderWidth: 2,\n");
        script.append("                fill: false,\n");
        script.append("                tension: 0.4,\n");
        script.append("                pointRadius: 0\n");
        script.append("            },\n");
        script.append("            {\n");
        script.append("                label: 'Promedio',\n");
        script.append("                data: avgLine").append(idSuffix).append(",\n");
        script.append("                borderColor: 'rgba(255, 99, 132, 0.8)',\n");
        script.append("                borderWidth: 2,\n");
        script.append("                borderDash: [5, 5],\n");
        script.append("                fill: false,\n");
        script.append("                pointRadius: 0\n");
        script.append("            },\n");
        script.append("        ]\n");
        script.append("    },\n");
        script.append("    options: {\n");
        script.append("        responsive: true,\n");
        script.append("        maintainAspectRatio: false,\n");
        script.append("        interaction: { mode: 'index', intersect: false },\n");
        script.append("        plugins: {\n");
        script.append("            title: {\n");
        script.append("                display: true,\n");
        script.append("                text: 'Evolución de la Diversidad de la Población',\n");
        script.append("                font: { size: 18 }\n");
        script.append("            },\n");
        script.append("            legend: { position: 'top' },\n");
        script.append("            tooltip: {\n");
        script.append("                callbacks: {\n");
        script.append("                    label: function(context) {\n");
        script.append("                        return context.dataset.label + ': ' + context.parsed.y.toFixed(4);\n");
        script.append("                    }\n");
        script.append("                }\n");
        script.append("            }\n");
        script.append("        },\n");
        script.append("        scales: {\n");
        script.append("            x: { title: { display: true, text: 'Generación' } },\n");
        script.append("            y: { title: { display: true, text: 'Diversidad' }, beginAtZero: false }\n");
        script.append("        }\n");
        script.append("    }\n");
        script.append("});\n");

        return script.toString();
    }

    //Formetea una lista a string para usarla en JS
    private static String formatList(List<?> list) {
        return list.toString();
    }
}
