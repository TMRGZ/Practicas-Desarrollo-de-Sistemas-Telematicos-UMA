/////////  Manual
Iniciar ChatServer.java junto a un cliente TCP, al enviar un mensaje lo recibirán el resto de clientes conectados

//////// No es necesario un fichero de muestra

//////// Diseño
Una clase main con tres métodos: nuevaPeticion(), gestionarPeticion() y procesarPeticion()

//////// Critica
    + Operaciones concurrentes automatizadas con Selector
    - Claridad de Selector no muy conseguida
    - Manejar ByteBuffer puede ser algo engorroso