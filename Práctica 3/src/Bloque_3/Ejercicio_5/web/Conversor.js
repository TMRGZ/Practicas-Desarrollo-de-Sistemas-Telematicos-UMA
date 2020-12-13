function convertirLongitudes() {
    var origen = document.getElementById("origenLongitud").value;
    var destino = document.getElementById("destinoLongitud").value;
    var valor = parseFloat(document.getElementById("numeroLongitud").value);

    if (origen === "metros") {
        if (destino === "metros") {
            document.getElementById("resultLongitud").innerHTML = valor.toString();
        } else if (destino === "pies") {
            document.getElementById("resultLongitud").innerHTML = (valor * 3.281).toString();
        } else if (destino === "pulgadas") {
            document.getElementById("resultLongitud").innerHTML = (valor * 39.37).toString();
        }
    } else if (origen === "pies") {
        if (destino === "metros") {
            document.getElementById("resultLongitud").innerHTML = (valor / 3.281).toString();
        } else if (destino === "pies") {
            document.getElementById("resultLongitud").innerHTML = valor.toString();
        } else if (destino === "pulgadas") {
            document.getElementById("resultLongitud").innerHTML = (valor * 12).toString();
        }
    } else if (origen === "pulgadas") {
        if (destino === "metros") {
            document.getElementById("resultLongitud").innerHTML = (valor / 39.37).toString();
        } else if (destino === "pies") {
            document.getElementById("resultLongitud").innerHTML = (valor / 12).toString();
        } else if (destino === "pulgadas") {
            document.getElementById("resultLongitud").innerHTML = valor.toString();
        }
    }
}

function convertirPesos() {
    var origen = document.getElementById("origenPeso").value;
    var destino = document.getElementById("destinoPeso").value;
    var valor = parseFloat(document.getElementById("numeroPeso").value);

    if (origen === "gramos") {
        if (destino === "gramos") {
            document.getElementById("resultPeso").innerHTML = valor.toString();
        } else if (destino === "libras") {
            document.getElementById("resultPeso").innerHTML = (valor / 453.592).toString();
        } else if (destino === "onzas") {
            document.getElementById("resultPeso").innerHTML = (valor / 28.35).toString();
        }
    } else if (origen === "libras") {
        if (destino === "gramos") {
            document.getElementById("resultPeso").innerHTML = (valor * 453.592).toString();
        } else if (destino === "libras") {
            document.getElementById("resultPeso").innerHTML = valor.toString();
        } else if (destino === "onzas") {
            document.getElementById("resultPeso").innerHTML = (valor * 16).toString();
        }
    } else if (origen === "onzas") {
        if (destino === "gramos") {
            document.getElementById("resultPeso").innerHTML = (valor * 28.35).toString();
        } else if (destino === "libras") {
            document.getElementById("resultPeso").innerHTML = (valor / 16).toString();
        } else if (destino === "onzas") {
            document.getElementById("resultPeso").innerHTML = valor.toString();
        }
    }
}