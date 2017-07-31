<?php
	include('../security.php');

	$resultado = file_get_contents("https://api.thingspeak.com/channels/201792/feeds.json?api_key=2YP59UR8MCB78VDJ");

	function grafico1($data1, $data2, $qtd, $pag){
		global $resultado;

		$var = json_decode($resultado)->{'feeds'};
		$var2 = array();
		$j = 0;

		for($i = 0; $i < count($var); $i++){
			$x = $var[$i]->{'field1'};
			$x = str_replace('-', '', $x);

			if($data1 <= $x && $data2 >= $x){
				$var2[$j] = $var[$i];
				$j++;
			}
		}

		$var3 = array();
		$k = 0;
		$c1 = $qtd * $pag;
		$c2 = $c1 + $qtd;

		for($i = 0; $i < count($var2); $i++){
			if($c1 <= $i && $c2 > $i){
				$var3[$k] = $var2[$i];
				$k++;
			}
		}

		$res = array();
		$res[0] = json_encode($var3);
		$res[1] = count($var2);

		return $res;
	}

	function grafico2($d1, $d2, $h1, $h2, $v1, $v2, $qtd, $pag){
		global $resultado;

		$var = json_decode($resultado)->{'feeds'};
		$var2 = array();
		$j = 0;

		for($i = 0; $i < count($var); $i++){
			$x = $var[$i]->{'field1'};
			$x = str_replace('-', '', $x);
			$y = $var[$i]->{'field2'};
			$y = str_replace(':', '', $y);
			$z = $var[$i]->{'field4'};

			if($d1 <= $x && $d2 >= $x){
				if($h1 <= $y && $h2 >= $y){
					if($v1 <= $z && $v2 >= $z){
						$var2[$j] = $var[$i];
						$j++;
					}
				}
			}
		}

		$var3 = array();
		$k = 0;
		$c1 = $qtd * $pag;
		$c2 = $c1 + $qtd;

		for($i = 0; $i < count($var2); $i++){
			if($c1 <= $i && $c2 > $i){
				$var3[$k] = $var2[$i];
				$k++;
			}
		}

		$res = array();
		$res[0] = json_encode($var3);
		$res[1] = count($var2);

		return $res;
	}

	function gen_data1($dados, $eixo){
		if($eixo == 0){
			$dados = fix_data($dados);
		}

		$data = '[{';

		$var = json_decode($dados);

		if($eixo == 0){
			for($i = 0; $i < count($var); $i++){
				$data .= '"label": "' . $var[$i]->{'field1'} . '", "value": "' . $var[$i]->{'field4'} . '"';
				$data .= $i == (count($var) - 1) ? '' : '},{';
			}
		}
		else if($eixo == 1){
			for($i = 0; $i < count($var); $i++){
				$data .= '"label": "' . $var[$i]->{'field2'} . '", "value": "' . $var[$i]->{'field4'} . '"';
				$data .= $i == (count($var) - 1) ? '' : '},{';
			}
		}

		$data .= '}]';

		return $data;
	}

	function listar_dias($d1, $d2){
		global $resultado;

		$var = json_decode($resultado)->{'feeds'};
		$dias = array();
		$j = 0;

		for($i = 0; $i < count($var); $i++){
			$x = str_replace('-', '', $var[$i]->{'field1'});

			if($d1 <= $x && $d2 >= $x){
				if(!in_array($x, $dias)){
					$dias[$j] = $x;
					$j++;
				}
			}
		}

		return $dias;
	}

	function listar_meses($d1, $d2){
		global $resultado;

		$var = json_decode($resultado)->{'feeds'};
		$meses = array();
		$j = 0;

		for($i = 0; $i < count($var); $i++){
			$x = str_replace('-', '', $var[$i]->{'field1'});
			$y = substr($x, 0, 6);

			if($d1 <= $x && $d2 >= $x){
				if(!in_array($y, $meses)){
					$meses[$j] = $y;
					$j++;
				}
			}
		}

		return $meses;
	}

	function medias_d($dias, $h1 = '000000', $h2 = '235959', $v1 = 0, $v2 = 9999999999){
		global $resultado;

		$var = json_decode($resultado)->{'feeds'};
		$res = array();

		for($i = 0; $i < count($dias); $i++){
			$total = 0;
			$c = 0;

			for($j = 0; $j < count($var); $j++){
				$x = str_replace('-', '', $var[$j]->{'field1'});
				$y = str_replace(':', '', $var[$j]->{'field2'});
				$z = $var[$j]->{'field4'};
				if($x == $dias[$i]){
					if($h1 <= $y && $h2 >= $y){
						if($v1 <= $z && $v2 >= $z){
							$total += $z;
							$c++;
						}
					}
				}
			}

			$res[$i] = $c == 0 ? $c : ($total / $c);
		}

		return $res;
	}

	function medias_m($meses, $h1 = '000000', $h2 = '235959', $v1 = 0, $v2 = 9999999999){
		global $resultado;

		$var = json_decode($resultado)->{'feeds'};
		$res = array();

		for($i = 0; $i < count($meses); $i++){
			$total = 0;
			$c = 0;

			for($j = 0; $j < count($var); $j++){
				$x = substr(str_replace('-', '', $var[$j]->{'field1'}), 0, 6);
				$y = str_replace(':', '', $var[$j]->{'field2'});
				$z = $var[$j]->{'field4'};
				if($x == $meses[$i]){
					if($h1 <= $y && $h2 >= $y){
						if($v1 <= $z && $v2 >= $z){
							$total += $z;
							$c++;
						}
					}
				}
			}

			$res[$i] = $c == 0 ? $c : ($total / $c);
		}

		return $res;
	}

	function filtro_medias($valores, $medias, $qtd, $pag){
		$res = array();
		$i = 0;
		$c1 = $qtd * $pag;
		$c2 = $c1 + $qtd;

		for($j = 0; $j < count($medias); $j++){
			if($c1 <= $j && $c2 > $j){
				$res[$i] = array();

				if(strlen($valores[$j]) == 6) $v = substr($valores[$j], 4, 2) . '/' . substr($valores[$j], 0, 4);
				else if(strlen($valores[$j] == 8)) $v = substr($valores[$j], 6, 2) . '/' . substr($valores[$j], 4, 2) . '/' . substr($valores[$j], 0, 4);
				else $v = $valores[$j];

				$res[$i][0] = $v;
				$res[$i][1] = $medias[$j];
				$i++;
			}
		}

		return $res;
	}

	function gen_data2($dados){
		$data = '[{';

		for($i = 0; $i < count($dados); $i++){
			$data .= '"label":"' . $dados[$i][0] . '","value":"' . $dados[$i][1] . '"';
			$data .= $i == (count($dados) - 1) ? '' : '},{';
		}

		$data .= '}]';

		return $data;
	}

	function fix_data($dados){
		$var = json_decode($dados);

		for($i = 0; $i < count($var); $i++){
			$x = $var[$i]->{'field1'};
			$tmp = explode('-', $x);
			$x = $tmp[2] . '/' . $tmp[1] . '/' . $tmp[0];
			$var[$i]->{'field1'} = $x;
		}

		return json_encode($var);
	}
?>