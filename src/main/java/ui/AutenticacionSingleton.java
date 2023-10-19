package ui;

public class AutenticacionSingleton {
    private String authToken;
    
    private static final AutenticacionSingleton instance = new AutenticacionSingleton();
    
    private AutenticacionSingleton() {
        // Inicializa el token de autenticación u otras variables si es necesario.
    }
    
    public static AutenticacionSingleton getInstance() {
        return instance;
    }
    
    public String getAuthToken() {
        return authToken;
    }
    
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    
    // Agrega métodos adicionales según tus necesidades, como para cerrar sesión, etc.
}
