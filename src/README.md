# proyectoSistemasDistribuidos Videollamada en Java

Errores: 

Login - Serie 401-405

	peticion: Login nomUsuario contrase�a
	Respuesta en caso bueno: ok
	
	error 401: UsuarioNoEncontrado
	error 402: Contrase�a incorrecta	
		
Get - Serie 406-410

	peticion: Get nomUsuario
	Respuesta en caso bueno: ok nombreReal direccion
	
	error 406: Usuario pedido no existe
	
Add - Serie 411-415

	peticion: Add nomUsuario contrase�a nombreReal
	Respuesta en caso bueno: ok
	
	error 411: Usuario ya existe
	error 412: Contrase�a demasiado corta (< de 4 caracteres)
	
	
	
