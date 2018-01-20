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

	function check_hora($hora){
		$var = explode(':', $hora);
		
		if(count($var) != 3) return false;
		if(strlen($var[0]) != 2) return false;
		if(strlen($var[1]) != 2) return false;
		if(strlen($var[2]) != 2) return false;

		if($var[0] < 0 || $var[0] > 23) return false;
		if($var[1] < 0 || $var[1] > 59) return false;
		if($var[2] < 0 || $var[2] > 59) return false;

		return true;
	}
?>

<html>
	<head>
		<meta charset="UTF-8">
		<link rel='stylesheet' type='text/css'  href='../css/filtro_perso.css' />
	</head>
	<body>

		<?php
			if(isset($_POST['data1']) && isset($_POST['data2']) && isset($_POST['modo_d']) && isset($_POST['hora1']) && isset($_POST['hora2']) && isset($_POST['modo_h']) && isset($_POST['val1']) && isset($_POST['val2']) && isset($_POST['modo_v']) && isset($_POST['qtd']) && isset($_POST['eixo']) && isset($_POST['agr'])){
				$data1 = $_POST['data1'];
				$data2 = $_POST['data2'];
				$modo_d = $_POST['modo_d'];
				$hora1 = $_POST['hora1'];
				$hora2 = $_POST['hora2'];
				$modo_h = $_POST['modo_h'];
				$val1 = $_POST['val1'];
				$val2 = $_POST['val2'];
				$modo_v = $_POST['modo_v'];
				$qtd = $_POST['qtd'];
				$eixo = $_POST['eixo'];
				$agr = $_POST['agr'];

				$err = 0;

				if($modo_d != 0){
					if(!check_data($data1)){
						print '<script>alert("DATA 1 INVÁLIDA!");</script>';
						$err++;
					}

					if($modo_d == 4)
						if(!check_data($data2)){
							print '<script>alert("DATA 2 INVÁLIDA!");</script>';
							$err++;
						}
				}

				if($modo_h != 0){
					if(!check_hora($hora1)){
						print '<script>alert("HORA 1 INVÁLIDA!");</script>';
						$err++;
					}

					if($modo_h == 4)
						if(!check_hora($hora2)){
							print '<script>alert("HORA 2 INVÁLIDA!");</script>';
							$err++;
						}
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
					print '<script>window.location="filtro_perso.php";</script>';
				}

				$d1 = '00000000';
				$d2 = date('Ymd');

				if($modo_d != 0){
					$var = explode('/', $data1);
					$d1 = $var[2] . $var[1] . $var[0];

					if($modo_d == 1) $d2 = $d1;
					else if($modo_d == 2) $d2 = date('Ymd');
					else if($modo_d == 3){
						$d2 = $d1;
						$d1 = '00000000';
					}
					else{
						$var = explode('/', $data2);
						$d2 = $var[2] . $var[1] . $var[0];
					}
				}

				$h1 = '000000';
				$h2 = '235959';

				if($modo_h != 0){
					$h1 = str_replace(':', '', $hora1);

					if($modo_h == 1) $h2 = $h1;
					else if($modo_h == 2) $h2 = '235959';
					else if($modo_h == 3){
						$h2 = $h1;
						$h1 = '000000';
					}
					else $h2 = str_replace(':', '', $hora2);
				}

				if(empty($val1)) $val1 = 0;
				if(empty($val2)) $val2 = 0;

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

				if($agr == 0){
					$loc = 'Location: grafico.php?mod=2';
				}
				else{
					$loc = $loc = 'Location: grafico.php?mod=3&agr=' . $agr;
				}

				$loc .= '&qtd=' . $qtd . '&pag=0&d1=' . $d1 . '&d2=' . $d2 . '&h1=' . $h1 . '&h2=' . $h2 . '&v1=' . $v1 . '&v2=' . $v2 . '&eixo=' . $eixo;

				if(!$err) header($loc);
			}
			else{
		?>

		<div class='filtro'>
			<center><B><p class='label'>Temperatura: Filtro personalizado</B></p></center>
			<form target='_self' method='post'>
				<p class='d1_l'>Data 1:</p>
				<input class='d1_f' type='text' name='data1' maxlength='10' />
				<p class='modo_d_l'>Modo:</p>
				<select class='modo_d_f' name='modo_d'>
					<option value='0'>Qualquer data</option>
					<option value='1'> = Data 1 </option>
					<option value='2'> >= Data 1 </option>
					<option value='3'> <= Data 1 </option>
					<option value='4'> Entre as duas datas </option>
				</select>
				<p class='d2_l'>Data 2:</p>
				<input class='d2_f' type='text' name='data2' maxlength='10' />

				<p class='h1_l'>Hora 1:</p>
				<input class='h1_f' type='text' name='hora1' maxlength='8' />
				<p class='modo_h_l'>Modo:</p>
				<select class='modo_h_f' name='modo_h'>
					<option value='0'>Qualquer hora</option>
					<option value='1'> = Hora 1 </option>
					<option value='2'> >= Hora 1 </option>
					<option value='3'> <= Hora 1 </option>
					<option value='4'> Entre as duas horas </option>
				</select>
				<p class='h2_l'>Hora 2:</p>
				<input class='h2_f' type='text' name='hora2' maxlength='8' />

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

				<p class='qtd_l'>Quantidade de registros por página:</p>
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
