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
	
ConnectTo - Serie 416-420

	peticion: ConnectTo nomUsuario
	Respuesta en caso bueno: ok inetAddressDestino
	
	error 416: Usuario no existe
	error 417: Usuario no est� conectado
	
Disconnect - Serie 421-425

	peticion: Disconnect nomUsuario
	Respuesta en caso bueno: ok (y adem�s hace que la direccion del usuario con NomUsuario sea "")
	
	error 421: nomUsuario para el que se pide la desconexi�n no es el mismo que el que hace la petici�n
	error 422: Usuario de la petici�n no existe.
	
Connect - Serie 426-430

	peticion: Connect nomUsuario
	Respuesta en caso bueno: ok
	
	error 426: nomUsuario para el que se pide la conexi�n no es el mismo que el que hace la petici�n
	error 427: Usuario de la petici�n no existe.
	

	
	
	
