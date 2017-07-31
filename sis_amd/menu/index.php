<?php
	include('security.php');
?>

<html>
	<head>
		<meta charset="UTF-8">
		<title>Sistema de Monitoramento</title>
		<link rel="stylesheet" type="text/css"  href="css/style.css" />
	</head>

	<script>
		function usuarios(){
			document.getElementById('frame').setAttribute('src', 'cns_usuarios.php');
		}

		function t_mensal(){
			document.getElementById('frame').setAttribute('src', 'temp/filtro_mensal.php');
		}

		function t_data(){
			document.getElementById('frame').setAttribute('src', 'temp/filtro_data.php');
		}

		function t_perso(){
			document.getElementById('frame').setAttribute('src', 'temp/filtro_perso.php');
		}

		function l_mensal(){
			document.getElementById('frame').setAttribute('src', 'lum/filtro_mensal.php');
		}

		function l_data(){
			document.getElementById('frame').setAttribute('src', 'lum/filtro_data.php');
		}

		function l_perso(){
			document.getElementById('frame').setAttribute('src', 'lum/filtro_perso.php');
		}
	</script>

	<body>
		<nav>
			<ul class="menu">
				<li><a href="#">Ver dados do sensor de temperatura</a>
					<ul>
						<li onclick='t_mensal();'><a href="#">Filtro mensal básico</a></li>
						<li onclick='t_data();'><a href="#">Filtro de data básico</a></li>
						<li onclick='t_perso();'><a href="#">Filtro personalizado</a></li>
					</ul>
				</li>
				<li><a href="#">Ver dados do sensor de luminosidade</a>
					<ul>
						<li onclick='l_mensal();'><a href="#">Filtro mensal básico</a></li>
						<li onclick='l_data();'><a href="#">Filtro de data básico</a></li>
						<li onclick='l_perso();'><a href="#">Filtro personalizado</a></li>
					</ul>
				</li>
				<li onclick='usuarios();'><a href="#">Usuários</a></li>
				<li><a href="index.php?exit=1">Sair</a></li>

			</ul>
		</nav>

		<iframe id='frame' class='frame'></iframe>
	</body>
</html>

<?php
	if(isset($_GET['exit'])){
		session_destroy();
		header('Location: ../index.php');
	}
?>
