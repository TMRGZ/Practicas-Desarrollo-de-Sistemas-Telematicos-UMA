///// Manual
    Iniciar el servidor, puede responder a TCP y UDP

///// Critica
    + El uso de Selector facilita enormemente la gestion concurrente
    - La gestion de UDP es mas engorrosa que la de TCP
    - Es posible que el servidor crashee al desconectarse el cliente forzosamente