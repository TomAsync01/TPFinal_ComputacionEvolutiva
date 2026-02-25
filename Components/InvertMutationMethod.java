package Components;

import Model.Path;

import java.util.ArrayList;
import java.util.Random;

public class InvertMutationMethod implements MutationMethod {
    private final Random random;

    public InvertMutationMethod(Random random) {
        this.random = random;
    }

    @Override
    public Path mutate(Path sujeto) {
        ArrayList<Integer> ciudades = sujeto.getCities();
        int tamanio = ciudades.size();

        // Elegir dos posiciones al azar
        int posicion1 = random.nextInt(tamanio);
        int posicion2 = random.nextInt(tamanio);

        // Asegurarse que posicion1 <= posicion2
        if (posicion1 > posicion2) {
            int temp = posicion1;
            posicion1 = posicion2;
            posicion2 = temp;
        }

        // Invertir el segmento entre posicion1 y posicion2 (incluyéndolas)
        while (posicion1 < posicion2) {
            // Intercambiar elementos en posicion1 y posicion2
            Integer temp = ciudades.get(posicion1);
            ciudades.set(posicion1, ciudades.get(posicion2));
            ciudades.set(posicion2, temp);

            // Avanzar hacia el centro de la lista
            posicion1++;
            posicion2--;
        }

        // Recalcular el costo y fitness después de la mutación
        sujeto.recalculate();

        return sujeto;
    }

    @Override
    public String getName(){
        return "Inversión";
    }
}
