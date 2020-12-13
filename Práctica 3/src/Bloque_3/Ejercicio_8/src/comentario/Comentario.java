package comentario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Comentario implements Comparable<Comentario> {
    private String autor;
    private String fecha;
    private String contenido;

    public Comentario(String autor, String fecha, String contenido) {
        this.autor = autor;
        this.fecha = fecha;
        this.contenido = contenido;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comentario that = (Comentario) o;

        if (autor != null ? !autor.equals(that.autor) : that.autor != null) return false;
        if (fecha != null ? !fecha.equals(that.fecha) : that.fecha != null) return false;
        return contenido != null ? contenido.equals(that.contenido) : that.contenido == null;
    }

    @Override
    public int hashCode() {
        int result = autor != null ? autor.hashCode() : 0;
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        result = 31 * result + (contenido != null ? contenido.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Comentario o) {

        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(o.getFecha());
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFecha());

            return date1.compareTo(date2);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return 0;
    }
}

class ComentarioComparator implements Comparator<Comentario> {
    @Override
    public int compare(Comentario o1, Comentario o2) {
        return o1.compareTo(o2);
    }
}