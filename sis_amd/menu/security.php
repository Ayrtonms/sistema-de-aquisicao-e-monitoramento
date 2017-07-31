<?php
	session_start();
	if(!isset($_SESSION['logado'])){
		print '<script>alert("ACESSO NEGADO!");window.location="http://localhost/sis_amd/";</script>';
	}
?>