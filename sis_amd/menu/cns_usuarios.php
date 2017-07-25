<?php
	include('security.php');
?>

<html>
	<head>
		<meta charset="UTF-8">
		<link rel='stylesheet' type='text/css'  href='css/cns_usuarios.css' />
	</head>
	<body>
		<table class='table1'>
			<tr>
				<td class='td1'>
					<table class='table2'>
						<tr>
							<th class='td_id'></th>
							<th>Nome</th>
							<th>Login</th>
						</tr>

						<?php
							if(isset($_GET['err'])){
								print '<script>alert("ERRO AO CONECTAR COM O BANCO DE DADOS!");</script>';
							}
							else{
								$serv = 'localhost';
								$user = 'root';
								$pass = '123';
								$base = 'sis_amd';

								$limit = 0;
								$ant = "";
								$prx = "";

								$db = new mysqli($serv, $user, $pass, $base);

								if($db->connect_error){
									header('Location: cns_usuarios.php?err=1');
								}

								if(isset($_GET['limit'])){
									$limit = $_GET['limit'];
								}

								$sql = $db->prepare('select id_usuario, nome, login from usuarios limit ?,10');
								$sql->bind_param('i', $limit);
								$sql->execute();
								$sql->bind_result($id, $nome, $login);

								$i = 0;
								while($sql->fetch()){
									$cor = $i % 2 == 1 ? '#F1F5FA' : '#FFF';

									$field = '<img src="img/edit.png" onclick="window.location=\'frm_usuarios.php?id=' . $id . '\';">';

									print '<tr class ="tr2" bgcolor=' . $cor . '>';
									print '<td class="td_id">'. $field. '</td>';
									print '<td class="td2">'. $nome. '</td>';
									print '<td class="td2">'. $login. '</td>';
									print '</tr>';

									$i++;
								}

								$sql->free_result();

								$sql = $db->prepare("select count(id_usuario) from usuarios");
								$sql->execute();
								$sql->bind_result($count);

								if($sql->fetch()){
									if($limit >= 10){
										$ant = 'onclick="window.location=\'cns_usuarios.php?limit=' . ($limit - 10) . '\';"';
									}
									else if($limit != 0){
										$ant = 'onclick="window.location=\'cns_usuarios.php?limit=0\';"';
									}

									if($count >= $limit + 11){
										$prx = 'onclick="window.location=\'cns_usuarios.php?limit=' . ($limit + 10) . '\';"';
									}
								}

								$sql->free_result();
								$db->close();
							}
						?>

					</table>
				</td>
			</tr>
			<tr>
				<td class='td1'>
					<button class='btn_l' onclick='window.location="frm_usuarios.php";' >Novo</button>
					<button class='btn_l' <?php print $ant; ?> ><-</button>
					<button class='btn_l' <?php print $prx; ?> >-></button>
				</td>
			</tr>
		</div>
	</body>
</html>
