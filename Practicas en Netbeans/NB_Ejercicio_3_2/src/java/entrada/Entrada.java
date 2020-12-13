package entrada;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Entrada implements Comparable<Entrada> {
    private String titulo;
    private String autor;
    private String fecha;
    private String contenido;
    private String comentarios;

    public Entrada(String titulo, String autor, String fecha, String contenido, String comentarios) {
        this.titulo = titulo;
        this.autor = autor;
        this.fecha = fecha;
        this.contenido = contenido;
        this.comentarios = comentarios;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getFecha() {
        return fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public String getContenidoLimitado() {
        return contenido.substring(0, Math.min(contenido.length(), 100)).trim() + "...";
    }

    public String getComentarios() {
        return comentarios;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entrada entrada = (Entrada) o;

        return Objects.equals(titulo, entrada.titulo);
    }

    @Override
    public int hashCode() {
        return titulo != null ? titulo.hashCode() : 0;
    }

    @Override
    public int compareTo(Entrada e) {

        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(e.getFecha());
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFecha());

            return date1.compareTo(date2);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }


        return 0;
    }
}

class EntradaComparator implements Comparator<Entrada> {
    @Override
    public int compare(Entrada o1, Entrada o2) {
        return o1.compareTo(o2);
    }
}
