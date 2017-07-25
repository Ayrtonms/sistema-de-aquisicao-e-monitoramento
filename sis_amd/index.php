<html>
	<head>
		<meta charset="UTF-8">
		<title>Login - Sistema de Monitoramento</title>
		<link rel="stylesheet" type="text/css"  href="style.css" />
	</head>
	<body>
		<div class="login">
			<form target="_self" method="POST">
				<center><B>Sistema de Monitoramento</B></center>
				<p class='login_l'>Login:</p>
				<input class='login_f' type="text" name="login" />
				<p class='senha_l'>Senha:</p>
				<input class='senha_f' type="password" name="senha" />
				<center><input class="botao" type="submit" value="OK" /></center>
			</form>
		</div>
	</body>
</html>

<?php
	if(isset($_POST['login']) && isset($_POST['senha'])){
		$senha = $_POST['senha'];
		$a = true;

		$serv = 'localhost';
		$log = 'root';
		$pass = '123';
		$banco = 'sis_amd';

		$db = new mysqli($serv, $log, $pass, $banco);
		if($db->connect_error){
			header('Location: index.php?err=1');
		}

		$sql = $db->prepare('select id_usuario from usuarios where login = ? and senha = ?');
		$sql->bind_param('ss', $_POST['login'], $senha);
		$sql->execute();

		$sql->bind_result($usu);
		if(!$sql->fetch()){
			$a = false;
		}

		$sql->free_result();
		$db->close();

		if($a){
			session_start();
			$_SESSION['logado'] = true;
			header('Location: menu/index.php');
		}
		else{
			print '<script>alert("ACESSO NEGADO!");</script>';
		}
	}

	if(isset($_GET['err'])){
		print '<script>alert("ERRO AO CONECTAR COM O BANCO DE DADOS!");</script>';
	}
?>
