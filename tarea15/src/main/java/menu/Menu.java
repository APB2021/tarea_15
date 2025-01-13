package menu;

import java.io.File;
import java.sql.Connection;
import java.util.Scanner;

/**
 * Esta clase se encarga de gestionar el menú principal de la aplicación.
 * Contiene las opciones para interactuar con los grupos y los alumnos.
 */

public class Menu {

	private final Scanner sc = new Scanner(System.in); // Scanner para la entrada de datos
	private final GestorAlumnos gestorAlumnos = new GestorAlumnos(); // Gestor para los alumnos
	private final GestorGrupos gestorGrupos = new GestorGrupos(); // Gestor para los grupos

	/**
	 * Muestra el menú principal y permite seleccionar una opción. Permite al
	 * usuario seleccionar opciones como insertar alumnos, grupos, mostrar todos los
	 * alumnos y guardar los datos de los alumnos en un archivo de texto.
	 */

	public void mostrarMenu() {
		int opcion;

		do {
			System.out.println("---- Menú Principal ----");
			System.out.println("1. Insertar nuevo alumno");
			System.out.println("2. Insertar nuevo grupo");
			System.out.println("3. Mostrar todos los alumnos");
			System.out.println("4. Guardar todos los alumnos en un fichero de texto");
			System.out.println("5. Leer alumnos de un fichero de texto y guardarlos en la BD");
			System.out.println("6. Modificar el nombre de un alumno por su NIA");
			System.out.println("7. Eliminar un alumno a partir de su NIA");
			System.out.println("8. Eliminar los alumnos del grupo indicado");
			System.out.println("9. Guardar grupos y alumnos en un archivo XML");
			System.out.println("10. Leer un archivo XML de grupos y guardar los datos en la BD");

			System.out.println("0. Salir");
			System.out.println("-------------------------");
			System.out.print("Selecciona una opción: ");

			try {
				opcion = sc.nextInt();
				sc.nextLine(); // Limpiar buffer

				switch (opcion) {
				case 1:
					insertarNuevoAlumno();
					break;
				case 2:
					insertarNuevoGrupo();
					break;
				case 3:
					mostrarTodosLosAlumnos();
					break;
				case 4:
					guardarAlumnosEnFicheroTexto();
					break;
				case 5:
					leerAlumnosDesdeFichero();
					break;
				case 6:
					modificarNombreAlumnoPorNia();
					break;
				case 7:
					eliminarAlumnoPorNIA();
					;
					break;
				case 8:
					eliminarAlumnosPorGrupo();
					break;
				case 9:
					guardarGruposEnXML();
					break;
				case 10:
					leerYGuardarGruposXML();
					break;
				case 0:
					System.out.println("Saliendo del programa...");
					break;
				default:
					System.out.println("Opción no válida. Intenta de nuevo.");
				}
			} catch (Exception e) {
				System.out.println("Entrada no válida. Por favor, introduce un número.");
				sc.nextLine(); // Limpiar el buffer en caso de error
				opcion = 0; // Reiniciar la opción para evitar salir del bucle
			}
		} while (opcion != 0); // Modificado para que salga correctamente con la opción 0
	}

	/**
	 * Permite insertar un nuevo alumno solicitando los datos al usuario y
	 * almacenándolos en la base de datos.
	 */
	private void insertarNuevoAlumno() {
		Connection conexionBD = null;

		try {
			Alumno alumno = gestorAlumnos.solicitarDatosAlumno();
			conexionBD = ConexionBDMySQL.getConexion();

			if (gestorAlumnos.insertarAlumno(conexionBD, alumno)) {
				System.out.println("Alumno insertado correctamente.");
			} else {
				System.out.println("Error al insertar el alumno.");
			}
		} catch (Exception e) {
			System.out.println("Ocurrió un error al insertar el alumno: " + e.getMessage());
		} finally {
			try {
				if (conexionBD != null && !conexionBD.isClosed()) {
					conexionBD.close();
					System.out.println("Conexión a la base de datos cerrada.");
				}
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
	}

	/**
	 * Permite insertar un nuevo grupo solicitando los datos al usuario y
	 * almacenándolos en la base de datos.
	 */
	private void insertarNuevoGrupo() {
		Connection conexionBD = null;
		String nombreGrupo;

		try {
			// Pedimos al usuario el nombre del nuevo grupo con validación
			while (true) {
				System.out.println("Introduce el nombre del nuevo grupo (una letra):");
				nombreGrupo = sc.nextLine().toUpperCase().trim();

				// Validamos que el nombre sea solo una letra
				if (nombreGrupo.length() == 1 && nombreGrupo.matches("[A-Za-z]")) {
					break; // Salimos del bucle si la validación es exitosa
				} else {
					System.out.println("El nombre del grupo debe ser una sola letra.");
				}
			}

			// Crear objeto Grupo
			Grupo grupo = new Grupo(nombreGrupo);

			// Obtener la conexión a la base de datos
			conexionBD = ConexionBDMySQL.getConexion();

			// Intentamos insertar el nuevo grupo
			if (gestorGrupos.insertarGrupo(conexionBD, grupo)) {
				System.out.println("Grupo insertado correctamente.");
			} else {
				System.out.println("Error al insertar el grupo.");
			}
		} catch (Exception e) {
			System.out.println("Ocurrió un error al insertar el grupo: " + e.getMessage());
		} finally {
			try {
				if (conexionBD != null && !conexionBD.isClosed()) {
					conexionBD.close();
					System.out.println("Conexión a la base de datos cerrada.");
				}
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
	}

	private void mostrarTodosLosAlumnos() {
		try (Connection conexionBD = ConexionBDMySQL.getConexion()) {
			if (gestorAlumnos.mostrarTodosLosAlumnos(conexionBD)) {
				System.out.println("Los alumnos se han mostrado correctamente.");
			} else {
				System.out.println("No se pudieron mostrar los alumnos.");
			}
		} catch (Exception e) {
			System.out.println("Ocurrió un error al mostrar los alumnos: " + e.getMessage());
		}
	}

	/**
	 * Permite guardar todos los alumnos en un archivo de texto. Recupera la
	 * información de los alumnos de la base de datos y la guarda en un archivo
	 * llamado "alumnos.txt". La información incluye: nombre, apellidos, género,
	 * fecha de nacimiento, ciclo, curso y nombre del grupo.
	 */
	private void guardarAlumnosEnFicheroTexto() {
		try {
			// Recuperamos todos los alumnos
			Connection conexionBD = ConexionBDMySQL.getConexion();

			// Llamamos al método que guarda los alumnos en el fichero de texto sin esperar
			// un valor booleano
			gestorAlumnos.guardarAlumnosEnFicheroTexto(conexionBD);

			System.out.println("Alumnos guardados correctamente en el archivo de texto.");

		} catch (Exception e) {
			System.out.println("Ocurrió un error al guardar los alumnos en el archivo de texto: " + e.getMessage());
		}
	}

	/**
	 * Permite leer alumnos desde el fichero fijo "alumnos.txt" y guardarlos en la
	 * base de datos.
	 */
	private void leerAlumnosDesdeFichero() {
		try {
			Connection conexionBD = ConexionBDMySQL.getConexion();

			if (gestorAlumnos.leerAlumnosDeFicheroTexto(conexionBD)) {
				System.out.println("Alumnos leídos e insertados correctamente desde el fichero 'alumnos.txt'.");
			} else {
				System.out.println("Ocurrió un error al procesar el fichero.");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Permite modificar el nombre de un alumno solicitando su NIA y el nuevo
	 * nombre.
	 */
	private void modificarNombreAlumnoPorNia() {
		Connection conexionBD = null;
		try {
			// Solicitar al usuario el NIA del alumno
			System.out.print("Introduce el NIA del alumno cuyo nombre quieres modificar: ");
			int nia = sc.nextInt();
			sc.nextLine(); // Limpiar buffer

			// Solicitar el nuevo nombre del alumno
			System.out.print("Introduce el nuevo nombre para el alumno: ");
			String nuevoNombre = sc.nextLine().trim().toUpperCase();

			// Validar que el nombre no esté vacío
			if (nuevoNombre.isEmpty()) {
				System.out.println("El nombre no puede estar vacío.");
				return;
			}

			// Conectar a la base de datos
			conexionBD = ConexionBDMySQL.getConexion();

			// Llamar al método del gestor para modificar el nombre
			if (gestorAlumnos.modificarNombreAlumnoPorNia(conexionBD, nia, nuevoNombre)) {
				System.out.println("Nombre del alumno modificado correctamente.");
			} else {
				System.out.println("No se pudo modificar el nombre del alumno. Verifica el NIA.");
			}
		} catch (Exception e) {
			System.out.println("Ocurrió un error al modificar el nombre del alumno: " + e.getMessage());
		} finally {
			try {
				if (conexionBD != null && !conexionBD.isClosed()) {
					conexionBD.close();
					System.out.println("Conexión a la base de datos cerrada.");
				}
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
	}

	/**
	 * Permite eliminar un alumno de la base de datos a partir de su NIA (PK).
	 */
	private void eliminarAlumnoPorNIA() {
		Connection conexionBD = null;

		try {
			// Solicitar NIA al usuario
			System.out.println("Introduce el NIA del alumno a eliminar:");
			int nia = sc.nextInt();
			sc.nextLine(); // Limpiar buffer

			// Obtener la conexión a la base de datos
			conexionBD = ConexionBDMySQL.getConexion();

			// Intentar eliminar el alumno
			if (gestorAlumnos.eliminarAlumnoPorNIA(conexionBD, nia)) {
				System.out.println("Alumno eliminado correctamente.");
			} else {
				System.out.println("No se encontró un alumno con el NIA proporcionado.");
			}
		} catch (Exception e) {
			System.out.println("Ocurrió un error al intentar eliminar el alumno: " + e.getMessage());
		} finally {
			try {
				if (conexionBD != null && !conexionBD.isClosed()) {
					conexionBD.close();
					System.out.println("Conexión a la base de datos cerrada.");
				}
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
	}

	/**
	 * Elimina los alumnos del grupo indicado por el usuario. Muestra previamente
	 * los grupos existentes y permite al usuario seleccionar uno. Luego elimina a
	 * todos los alumnos que pertenezcan al grupo seleccionado.
	 */
	private void eliminarAlumnosPorGrupo() {
		Connection conexionBD = null;

		try {
			// Establecemos conexión con la base de datos
			conexionBD = ConexionBDMySQL.getConexion();

			// Mostramos los grupos disponibles
			System.out.println("Grupos disponibles:");
			if (!gestorGrupos.mostrarTodosLosGrupos(conexionBD)) {
				System.out.println("No hay grupos registrados.");
				return;
			}

			// Pedimos al usuario el nombre del grupo a eliminar
			System.out.println("Introduce el nombre del grupo cuyos alumnos deseas eliminar:");
			String nombreGrupo = sc.nextLine().toUpperCase().trim();

			// Confirmamos la operación con el usuario
			System.out.println(
					"¿Estás seguro de que deseas eliminar todos los alumnos del grupo " + nombreGrupo + "? (S/N)");
			String confirmacion = sc.nextLine().toUpperCase().trim();

			if (!confirmacion.equals("S")) {
				System.out.println("Operación cancelada por el usuario.");
				return;
			}

			// Llamamos al gestor para realizar la operación
			if (gestorAlumnos.eliminarAlumnosPorGrupo(conexionBD, nombreGrupo)) {
				System.out.println("Alumnos del grupo " + nombreGrupo + " eliminados correctamente.");
			} else {
				System.out.println(
						"No se pudieron eliminar los alumnos del grupo especificado. Verifica el nombre del grupo.");
			}
		} catch (Exception e) {
			System.out.println("Ocurrió un error al eliminar alumnos por grupo: " + e.getMessage());
		} finally {
			try {
				if (conexionBD != null && !conexionBD.isClosed()) {
					conexionBD.close();
					System.out.println("Conexión a la base de datos cerrada.");
				}
			} catch (Exception e) {
				System.out.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
	}

	/**
	 * Método que se encarga de guardar los grupos y sus alumnos en un archivo XML.
	 * Utiliza la conexión a la base de datos proporcionada por la clase
	 * ConexionBDMySQL.
	 */
	private void guardarGruposEnXML() {
		// Variable para almacenar la conexión a la base de datos
		Connection conexionBD = null;

		try {
			// Obtenemos la conexión a la base de datos mediante la clase ConexionBDMySQL
			conexionBD = ConexionBDMySQL.getConexion();

			// Llamamos al método de GestorGrupos para guardar los grupos en el archivo XML
			if (gestorGrupos.guardarGruposEnXML(conexionBD)) {
				// Si el proceso es exitoso, mostramos un mensaje de éxito
				System.out.println("Archivo XML guardado correctamente.");
			} else {
				// Si hubo algún error, mostramos un mensaje de fallo
				System.out.println("Error al guardar el archivo XML.");
			}
		} catch (Exception e) {
			// Capturamos cualquier excepción y mostramos el mensaje de error
			System.out.println("Ocurrió un error al guardar los grupos en XML: " + e.getMessage());
		} finally {
			try {
				// Cerramos la conexión a la base de datos en el bloque finally
				if (conexionBD != null && !conexionBD.isClosed()) {
					conexionBD.close();
					System.out.println("Conexión a la base de datos cerrada.");
				}
			} catch (Exception e) {
				// Capturamos cualquier error al cerrar la conexión
				System.out.println("Error al cerrar la conexión: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Lee el archivo XML de grupos (alumnos.xml) y guarda los datos en la base de datos MySQL.
	 * Si ocurre un error durante el proceso, se captura la excepción y se muestra un mensaje de error.
	 */
	private void leerYGuardarGruposXML() {
	    // Variable para almacenar la conexión a la base de datos
	    Connection conexionBD = null;

	    try {
	        // Obtenemos la conexión a la base de datos mediante la clase ConexionBDMySQL
	        conexionBD = ConexionBDMySQL.getConexion();

	        // Ruta fija del archivo XML de grupos
	        String rutaArchivo = "grupos.xml";

	        // Verificamos si el archivo existe
	        File archivoXML = new File(rutaArchivo);
	        if (!archivoXML.exists()) {
	            System.out.println("El archivo XML no existe en la ruta especificada: " + rutaArchivo);
	            return; // Salimos del método si el archivo no existe
	        }

	        // Llamamos al método de GestorGrupos para leer los grupos en el archivo XML
	        if (GestorGrupos.leerYGuardarGruposXML(rutaArchivo, conexionBD)) {
	            // Si el proceso es exitoso, mostramos un mensaje de éxito
	            System.out.println("Archivo XML leído correctamente y datos guardados en la base de datos.");
	        } else {
	            // Si hubo un error en el proceso, mostramos un mensaje de fallo
	            System.out.println("Error al procesar el archivo XML.");
	        }
	        
	    } catch (Exception e) {
	        // Capturamos cualquier excepción y mostramos el mensaje de error
	        System.out.println("Ocurrió un error al leer los grupos en XML: " + e.getMessage());
	    } finally {
	        // Cerramos la conexión a la base de datos en el bloque finally
	        try {
	            if (conexionBD != null && !conexionBD.isClosed()) {
	                conexionBD.close();
	                System.out.println("Conexión a la base de datos cerrada.");
	            }
	        } catch (Exception e) {
	            System.out.println("Error al cerrar la conexión: " + e.getMessage());
	        }
	    }
	}

}
