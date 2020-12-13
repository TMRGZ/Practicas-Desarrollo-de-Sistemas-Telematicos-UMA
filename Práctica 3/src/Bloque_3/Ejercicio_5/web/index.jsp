<%--
  Created by IntelliJ IDEA.
  User: MiguelRuiz
  Date: 11/02/2019
  Time: 13:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Conversor</title>
    <script type="text/javascript" src="Conversor.js"></script>
</head>
<body>

<h1>Conversor de longitudes</h1>
<h2>Valor</h2>
<input type="text" id="numeroLongitud"/>
<h2>Origen</h2>
<select id="origenLongitud">
    <option value="metros">Metros</option>
    <option value="pies">Pies</option>
    <option value="pulgadas">Pulgadas</option>
</select>

<h2>Destino</h2>
<select id="destinoLongitud">
    <option value="metros">Metros</option>
    <option value="pies">Pies</option>
    <option value="pulgadas">Pulgadas</option>
</select>

<button type="button" onclick="convertirLongitudes()">Convertir</button>
<h2>Resultado</h2>
<h3 id="resultLongitud"></h3>



<h1>Conversor de Pesos</h1>
<h2>Valor</h2>
<input type="text" id="numeroPeso"/>
<h2>Origen</h2>
<select id="origenPeso">
    <option value="gramos">Gramos</option>
    <option value="libras">Libras</option>
    <option value="onzas">Onzas</option>
</select>

<h2>Destino</h2>
<select id="destinoPeso">
    <option value="gramos">Gramos</option>
    <option value="libras">Libras</option>
    <option value="onzas">Onzas</option>
</select>

<button type="button" onclick="convertirPesos()">Convertir</button>
<h2>Resultado</h2>
<h3 id="resultPeso"></h3>

</body>
</html>
