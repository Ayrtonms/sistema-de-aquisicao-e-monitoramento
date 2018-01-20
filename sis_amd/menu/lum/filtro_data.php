<?php
	include('../security.php');

	function check_data($data){
		$var = explode('/', $data);

		if(count($var) != 3) return false;
		if(strlen($var[0]) != 2) return false;
		if(strlen($var[1]) != 2) return false;
		if(strlen($var[2]) != 4) return false;

		if($var[0] < 0 || $var[0] > 31) return false;
		if($var[1] < 0 || $var[1] > 12) return false;
		if($var[2] < 0 || $var[2] > date('Y')) return false;

		return true;
	}
?>

<html>
	<head>
		<meta charset="UTF-8">
		<link rel='stylesheet' type='text/css'  href='../css/filtro_data.css' />
	</head>
	<body>

		<?php
			if(isset($_POST['data1']) && isset($_POST['data2']) && isset($_POST['modo']) && isset($_POST['qtd']) && isset($_POST['eixo']) && isset($_POST['agr'])){
				$data1 = $_POST['data1'];
				$data2 = $_POST['data2'];
				$modo = $_POST['modo'];
				$qtd = $_POST['qtd'];
				$eixo = $_POST['eixo'];
				$agr = $_POST['agr'];

				$err = 0;

				if(!check_data($data1)){
					print '<script>alert("DATA 1 INVÁLIDA!");</script>';
					$err++;
				}

				if($modo == 3)
					if(!check_data($data2)){
						print '<script>alert("DATA 2 INVÁLIDA!");</script>';
						$err++;
					}

				if(empty($qtd) || $qtd == 0){
					print '<script>alert("QUANTIDADE INVÁLIDA!");</script>';
					$err++;
				}

				if($qtd > 100){
					print '<script>alert("QUANTIDADE ESPECIFICADA MUITO ALTA!");</script>';
					$err++;
				}

				if($agr != 0 && $eixo != 0){
					print '<script>alert("AO AGRUPAR OS DADOS É NECESSÁRIA A DATA NO EIXO X!");</script>';
					$err++;
				}

				if($err){
					print '<script>window.location="filtro_data.php";</script>';
				}

				$var = explode('/', $data1);
				$d1 = $var[2] . $var[1] . $var[0];

				if($modo == 0)
					$d2 = $d1;
				else if($modo == 1)
					$d2 = date('Ymd');
				else if($modo == 2){
					$d2 = $d1;
					$d1 = '00000000';
				}
				else{
					$var = explode('/', $data2);
					$d2 = $var[2] . $var[1] . $var[0];
				}

				if($agr == 0){
					$loc = 'Location: grafico.php?mod=0';
				}
				else{
					$loc = 'Location: grafico.php?mod=1&agr=' . $agr;
				}

				$loc .= '&d1=' . $d1 . '&d2=' . $d2 . '&qtd=' . $qtd . '&pag=0' . '&eixo=' . $eixo;

				if (!$err) header($loc);
			}
			else{
		?>

		<div class='filtro'>
			<center><B><p class='label'>Luminosidade: Filtro de data básico</B></p></center>
			<form target='_self' method='post'>
				<p class='d1_l'>Data 1:</p>
				<input class='d1_f' type='text' name='data1' maxlength='10' />
				<p class='d2_l'>Data 2:</p>
				<input class='d2_f' type='text' name='data2' maxlength='10' />
				<p class='modo_l'>Modo:</p>
				<select class='modo_f' name='modo'>
					<option value='0'> = Data 1 </option>
					<option value='1'> >= Data 1 </option>
					<option value='2'> <= Data 1 </option>
					<option value='3'> Entre as duas datas </option>
				</select>
				<p class='qtd_l'>Quantidade de registros por gráfico:</p>
				<input class='qtd_f' type='text' name='qtd' onkeyup="this.value=this.value.replace(/[^\d]+/,'')" maxlength='3' />
				<p class='eixo_l'>Eixo X:</p>
				<select class='eixo_f' name='eixo'>
					<option value='0'>Data</option>
					<option value='1'>Hora</option>
				</select>
				<p class='agr_l'>Agrupamento:</p>
				<select class='agr_f' name='agr'>
					<option value='0'>Nenhum</option>
					<option value='1'>Diário</option>
					<option value='2'>Mensal</option>
				</select>
				<input class='ok' type='submit'	value='OK' />
			</form>
		</div>

		<?php
			}
		?>
	</body>
</html>
