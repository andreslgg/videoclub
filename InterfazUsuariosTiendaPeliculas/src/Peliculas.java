
public class Peliculas {

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }
    

    private int ID;
    private String titulo;
    private String categoria;
    private String actor;
    private String formato;
    
 
public void Peliculas(int ID, String titulo, String categoria, String actor, String formato){
    this.ID=ID;
    this.actor=actor;
    this.categoria=categoria;
    this.formato=formato;
    this.titulo=titulo;
}
   
    public Peliculas(){
        
    }
}
