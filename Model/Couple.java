package Model;

public class Couple{
    private final Path dad;
    private final Path mom;

    public Couple(Path padre1, Path padre2) {
        this.dad = padre1;
        this.mom = padre2;
    }

    public Path getPadre1() { return dad; }
    public Path getPadre2() { return mom; }
}
