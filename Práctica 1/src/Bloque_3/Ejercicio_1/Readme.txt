///// Manual
    Iniciar el servidor, el funcionamiento depende del puerto usado 1234 hace que funcione como echo, 4321 sirve para mandar mensajes al servidor

///// Critica
    + El uso de Selector facilita enormemente la gestion concurrente
    - Usar dos canales TCP dificulta la gestion de los if
    - Es posible que el servidor crashee al desconectarse el cliente forzosamente