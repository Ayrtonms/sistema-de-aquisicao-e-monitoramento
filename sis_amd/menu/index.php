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

		function p_mensal(){
			document.getElementById('frame').setAttribute('src', 'painel/filtro_mensal.php');
		}

		function p_data(){
			document.getElementById('frame').setAttribute('src', 'painel/filtro_data.php');
		}

		function p_perso(){
			document.getElementById('frame').setAttribute('src', 'painel/filtro_perso.php');
		}

		function s_mensal(){
			document.getElementById('frame').setAttribute('src', 'sensor/filtro_mensal.php');
		}

		function s_data(){
			document.getElementById('frame').setAttribute('src', 'sensor/filtro_data.php');
		}

		function s_perso(){
			document.getElementById('frame').setAttribute('src', 'sensor/filtro_perso.php');
		}
	</script>

	<body>
		<nav>
			<ul class="menu">
				<li><a href="#">Ver dados do painel</a>
					<ul>
						<li onclick='p_mensal();'><a href="#">Filtro mensal básico</a></li>
						<li onclick='p_data();'><a href="#">Filtro de data básico</a></li>
						<li onclick='p_perso();'><a href="#">Filtro personalizado</a></li>
					</ul>
				</li>
				<li><a href="#">Ver dados do sensor de luminosidade</a>
					<ul>
						<li onclick='s_mensal();'><a href="#">Filtro mensal básico</a></li>
						<li onclick='s_data();'><a href="#">Filtro de data básico</a></li>
						<li onclick='s_perso();'><a href="#">Filtro personalizado</a></li>
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
