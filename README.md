GET /api/v1/usuarios/{id} quedó en UserController. El filtro del token es JwtAuthenticationFilter y el 404 está en GlobalExceptionHandler.
POST /api/v1/auth/validate quedó en AuthController y AuthControllerImpl. La lógica está en AuthService y AuthServiceImpl, y la verificación del JWT en JwtService, método validarToken. El 401 está en GlobalExceptionHandler
