package controlador;

import dao.AlumnosBD;
import dao.IAlumnosDao;
import logs.InicializarLogs;
import vista.IVista;
import vista.VistaConsola;

public class Controlador {

	public static void main(String[] args) {

		// Requisito Miguel: "Los Modelos se crearán en el main".
		IAlumnosDao modelo = new AlumnosBD();
		IVista vista = new VistaConsola();
		new Controlador().ejecutar(modelo, vista);

	}

	public void ejecutar(IAlumnosDao modelo, IVista vista) {

		// Inicializar los logs
		InicializarLogs.inicializarLogs();

		// Llamar a la vista que contiene el menú principal en consola
		vista.mostrarMenu(modelo);
	}
}