<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#op{
	display: none;
}
</style>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#searchColumn").change(function(){
		var v = $(this).val();
		if(v == "price"){
			console.log("가격을 선택!");
			$("#op").css("display","inline");
		}else{
			$("#op").css("display","none");
		}
	});
});
</script>
</head>
<body>
	<h2>도서목록</h2>
	<!-- 가격으로도 검색이 가능하도록 기능을 추가 해 봅니다. -->
	<form action="listBook.do" method="post">
		<select name="searchColumn" id="searchColumn" >
			<option value="bookname">도서명</option>
			<option value="publisher">출판사</option>
			<option value="price">가격</option>
		</select> 
		
		<select name="op" id="op">
			<option value="=">=</option>
			<option value=">">></option>
			<option value="<"><</option>
			<option value=">=">>=</option>
			<option value="<="><=</option>
		</select> 
		
		<input type="search" name="keyword">
		<input type="submit" value="검색">
	</form>
	<table border="1" width="80%">
		<tr>
			<th>도서번호</th>
			<th>도서명</th>
			<th>출판사</th>
			<th>가격</th>
		</tr>
		<c:forEach var="b" items="${list }">
			<tr>
				<td>${b.bookid }</td>
				<td>${b.bookname }</td>
				<td>${b.publisher }</td>
				<td>${b.price }</td>
			</tr>
		</c:forEach>
	</table>
	
	<c:forEach var="i" begin="1" end="${totalPage }">
		<a href="listBook.do?pageNUM=${i }">${i }</a>&nbsp;
	</c:forEach>
</body>
</html>










