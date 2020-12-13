Cliente/Servidor en Java, con iniciar el programa ya puedes iniciar los comandos, comando help para mas informaci�n sobre su uso.
El ClienteGUI es la version con una interfaz sencilla apoyada por la salida por consola donde se pintara el diagrama mientras que en la GUI apareceran los porcentajes de perdida
Para poder utilizar la simulaci�n de paquetes perdidos hay que poner la variable booleana simulaci�n a true en la clase TFTPacket.

Dos ficheros de muestra: TestAscii.txt para probar el modo ascii y TestBinary para probar el modo octet del programa

Cliente y servidor dependen de la clase TFTPacket.java que da formato a todo los tipos de paquetes y su procesamiento.
La clase PaqueteTFTP es la antigua implementaci�n de una fabrica de paquetes usada con el c�digo comentado de los m�todos put y get del cliente
para as� mostrar la evoluci�n del c�digo 

Como bondad, el hecho de dar el formato en una clase aparte consigue limpiar el c�digo que gestiona el envio y rececpi�n, facilitando as�
su operaci�n y mantenimiento, el problema de esta implementaci�n es que la simulaci�n de perdida de paquetes puede afectar al WRQ y RRQ

+Facilidad de uso
+Gesti�n de casos sencilla

-UDP hace mas dif�cil el controlar los errores
-El dise�o de paquetes puede ser algo engorroso
