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
		<link rel='stylesheet' type='text/css'  href='../css/filtro_horario.css' />
	</head>
	<body>

		<?php
			if(isset($_POST['data']) && isset($_POST['qtd']) && isset($_POST['val1']) && isset($_POST['modo_v']) && isset($_POST['val2'])){
				$data = $_POST['data'];
				$qtd = $_POST['qtd'];
				$val1 = $_POST['val1'];
				$modo_v = $_POST['modo_v'];
				$val2 = $_POST['val2'];

				$err = 0;

				if(!check_data($data)){
					print '<script>alert("DATA INVÁLIDA!");</script>';
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

				if($err){
					print '<script>window.location="filtro_horario.php";</script>';
				}

				$v1 = 0;
				$v2 = 9999999999;

				if($modo_v != 0){
					$v1 = $val1;

					if($modo_v == 1) $v2 = $v1;
					else if($modo_v == 2) $v2 = 9999999999;
					else if($modo_v == 3){
						$v2 = $v1;
						$v1 = 0;
					}
					else $v2 = $val2;
				}

				$var = explode('/', $data);
				$date = $var[2] . '-' . $var[1] . '-' . $var[0];

				$loc = 'Location: grafico.php?mod=4';

				$loc .= '&date=' . $date . '&qtd=' . $qtd . '&pag=0' . '&eixo=0&v1=' . $v1 . '&v2=' . $v2;

				if (!$err) header($loc);
			}
			else{
		?>

		<div class='filtro'>
			<center><B><p class='label'>Luminosidade: Filtro horário</B></p></center>
			<form target='_self' method='post'>
				<p class='data_l'>Data:</p>
				<input class='data_f' type='text' name='data' maxlength='10' />
				<p class='qtd_l'>Quantidade de registros por gráfico:</p>
				<input class='qtd_f' type='text' name='qtd' onkeyup="this.value=this.value.replace(/[^\d]+/,'')" maxlength='3' />

				<p class='v1_l'>Valor 1:</p>
				<input class='v1_f' type='text' name='val1' onkeyup="this.value=this.value.replace(/[^\d]+/,'')" />
				<p class='modo_v_l'>Modo:</p>
				<select class='modo_v_f' name='modo_v'>
					<option value='0'>Qualquer valor</option>
					<option value='1'> = Valor 1 </option>
					<option value='2'> >= Valor 1 </option>
					<option value='3'> <= Valor 1 </option>
					<option value='4'> Entre os dois valores </option>
				</select>
				<p class='v2_l'>Valor 2:</p>
				<input class='v2_f' type='text' name='val2' onkeyup="this.value=this.value.replace(/[^\d]+/,'')" />

				<input class='ok' type='submit'	value='OK' />
			</form>
		</div>

		<?php
			}
		?>
	</body>
</html>
