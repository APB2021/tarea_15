package vista;

import dao.IAlumnosDao;

public interface IVista {

	public void mostrarMenu(IAlumnosDao modelo);

	// case 1:
	public void insertarNuevoAlumno(IAlumnosDao modelo);

	// case 2:
	public void insertarNuevoGrupo(IAlumnosDao modelo);

	// case 3:
	public void mostrarTodosLosAlumnos();

	// case 4:
	public void guardarAlumnosEnFicheroTexto();

	// case 5:
	public void leerAlumnosDesdeFichero();

	// case 6:
	public void modificarNombreAlumnoPorNia();

	// case 7:
	public void eliminarAlumnoPorNIA();

	// case 8:
	public void eliminarAlumnosPorGrupo();

	// case 9:
	public void guardarGruposEnXML();

	// case 10:
	public void leerYGuardarGruposXML();

}
