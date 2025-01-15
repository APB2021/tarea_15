package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlumnoService {
	private static final Logger logger = LogManager.getLogger(AlumnoService.class);

	public void obtenerAlumnoPorId(int id) {
		try {
			if (id <= 0) {
				throw new IllegalArgumentException("El ID del alumno debe ser mayor que cero.");
			}

			// Simula la operación para obtener un alumno
			logger.info("Buscando alumno con ID: " + id);
			// Aquí iría la lógica para acceder a la base de datos
			// Pero simulamos una excepción
			throw new RuntimeException("Error al acceder a la base de datos.");

		} catch (IllegalArgumentException e) {
			logger.error("Excepción de argumento: " + e.getMessage());
		} catch (RuntimeException e) {
			logger.error("Excepción durante la operación: " + e.getMessage(), e);
		}
	}
}
