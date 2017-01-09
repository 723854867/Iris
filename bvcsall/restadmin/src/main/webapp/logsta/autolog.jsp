<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
	<head>
	<title></title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<script type='text/javascript' src="./js/fp/vendor/jquery.ui.widget.js"></script>
	<script type='text/javascript' src="./js/fp/jquery.iframe-transport.js"></script>
	<script type='text/javascript' src="./js/fp/jquery.fileupload.js"></script>
	<script type='text/javascript' src="./js/admin/query_autolog.js"></script>
	</head>
	<body>
		<input type="hidden" value="<c:out value='${tableId}'/>" id="hidden_table_id"/>
		<table id="displayTable" data-options="border:false,toolbar:'#dataGridToolbar'"></table>
		<div id="dataGridToolbar" region="north" border="false" style="border-bottom: 1px solid #ddd; padding: 2px 5px;">
			<div data-options="region:'north',border:false" style="height: 40px; padding-top: 5px; overflow: hidden;">
				<h2 style="float: left; padding-left: 10px; margin: 1px"></h2>
			</div>
			<div>
				<table>
					<tr id="auto_query"></tr>
				</table>
			</div>
		</div>
	</body>
</html>