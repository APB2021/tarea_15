
package tarea_15;

import logs.InicializarLogs;
import menu.Menu;
import service.AlumnoService;

public class App {
	public static void main(String[] args) {
		// Inicializar los logs
		InicializarLogs.inicializarLogs();

		// Llamar al menú principal
		Menu menu = new Menu();
		menu.mostrarMenu();
		
		// Probar la gestión de excepciones
        AlumnoService alumnoService = new AlumnoService();
        alumnoService.obtenerAlumnoPorId(-1);  // Invoca el servicio con un ID inválido para provocar una excepción
	}
}
