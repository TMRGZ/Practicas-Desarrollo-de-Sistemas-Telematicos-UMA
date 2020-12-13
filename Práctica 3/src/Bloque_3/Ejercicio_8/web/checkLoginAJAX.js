function xmlhttpPost() {
    var hrq = false;

    if (window.XMLHttpRequest) {
        hrq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        hrq = new ActiveXObject("Microsoft.XMLHTTP");
    }

    hrq.open('GET', "", true);
    hrq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    hrq.onreadystatechange = function () {
        if (hre.readyState === 4 && this.status === 200) {
            //updatePage(hrq.responseText);
            var user = document.getElementById("usu");

            if (user.toString() === ""){
                document.getElementById("result").innerHTML = "Usuario vacio";
            }

        }
    };
    //hrq.send(getQueryString());
    hrq.send();

}

function getQueryString() {
    var form = document.forms['login'];
    var pass = form.password.value;

    if (pass === "") {
        return "Campos vacios"
    }

    return "";
}

function updatePage(str) {
    document.getElementById("result").innerHTML = str;
}
