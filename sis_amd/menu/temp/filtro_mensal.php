<?php
	include('../security.php');
?>

<html>
	<head>
		<meta charset="UTF-8">
		<link rel='stylesheet' type='text/css'  href='../css/filtro_mensal.css' />
	</head>
	<body>

		<?php
			if(isset($_POST['ano']) && isset($_POST['mes']) && isset($_POST['qtd']) && isset($_POST['eixo']) && isset($_POST['agr'])){
				$ano = $_POST['ano'];
				$mes = $_POST['mes'];
				$qtd = $_POST['qtd'];
				$eixo = $_POST['eixo'];
				$agr = $_POST['agr'];

				$err = 0;

				if($ano > date('Y')){
					print '<script>alert("ANO INVÁLIDO!");</script>';
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
					print '<script>window.location="filtro_mensal.php";</script>';
				}

				$data1 = $ano . $mes . '01';
				$data2 = $ano . $mes . '31';

				if($agr == 0){
					$loc = 'Location: grafico.php?mod=0';
				}
				else{
					$loc = 'Location: grafico.php?mod=1&agr=' . $agr;
				}

				$loc .= '&d1=' . $data1 . '&d2=' . $data2 . '&qtd=' . $qtd . '&pag=0' . '&eixo=' . $eixo;

				if (!$err) header($loc);
			}
			else{
		?>

		<div class='filtro'>
			<center><B><p class='label'>Temperatura: Filtro mensal básico</B></p></center>
			<form target='_self' method='post'>
				<p class='ano_l'>Ano:</p>
				<input class='ano_f' type='text' name='ano' onkeyup="this.value=this.value.replace(/[^\d]+/,'')" maxlength='4' />
				<p class='mes_l'>Mês:</p>
				<select class='mes_f' name='mes'>
					<option value='01'>Janeiro</option>
					<option value='02'>Fevereiro</option>
					<option value='03'>Março</option>
					<option value='04'>Abril</option>
					<option value='05'>Maio</option>
					<option value='06'>Junho</option>
					<option value='07'>Julho</option>
					<option value='08'>Agosto</option>
					<option value='09'>Setembro</option>
					<option value='10'>Outubro</option>
					<option value='11'>Novembro</option>
					<option value='12'>Dezembro</option>
				</select>
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
