
public class Datos {
    private int ID;
    private String Usuario;
    private String Contraseña;
    private boolean validar=false;
    private boolean validacion=false;

    public boolean isValidar() {
        return validar;
    }

    public void setValidar(boolean validar) {
        this.validar = validar;
    }

    public boolean isValidacion() {
        return validacion;
    }

    public void setValidacion(boolean validacion) {
        this.validacion = validacion;
    }
    public Datos(){
        
    }
            
    public Datos(int Id, String Us,String Con){
        this.ID=Id;
        this.Usuario=Us;
        this.Contraseña=Con;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String Contraseña) {
        this.Contraseña = Contraseña;
    }
  
}
