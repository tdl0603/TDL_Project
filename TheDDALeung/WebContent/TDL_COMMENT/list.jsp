<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/bootstrap.css" type="text/css" />
	<link rel="stylesheet" href="style.css" type="text/css" />
	<link rel="stylesheet" href="css/dark.css" type="text/css" />
	<link rel="stylesheet" href="css/font-icons.css" type="text/css" />
	<link rel="stylesheet" href="css/animate.css" type="text/css" />
	<link rel="stylesheet" href="css/magnific-popup.css" type="text/css" />

	<link rel="stylesheet" href="css/responsive.css" type="text/css" />
</head>
<body>
	
	<!-- 데이터의 댓글 유무 -->
		<div class="container" >
		<c:if test="${pgListC.count==0}">
			<table border="1" width="700" cellpadding="0" cellspacing="0"
				align="center">
				<tr>
					<td align="center">게시판에 저장된 글이 없습니다람쥐.</td>
				</tr>
			</table>
		</c:if>
		<c:if test="${pgListC.count > 0 }">
			
			<!-- 댓글 출력 테이블  -->
			<table class="table">
			
				<!-- 실질적으로 레코드를 출력시켜주는 부분  
						이벤트를 발생시킨 객체를 구분하는 키워드 -> this
				-->
				
				<c:set var="number" value="${pgListC.number}" />
				<c:forEach var="articleC" items="${articleListC}">
					<tr>
						
					
							<!-- 답변글인 경우 먼저 답변이미지를 부착시키는 코드부분 --> 
							<c:if test="${articleC.TPC_level > 0 }">
								<img src="images/level.gif" width="${7*articleC.TPC_level }"
									height="16">
					
							</c:if>
							<c:if test="${articleC.TPC_level==0}">
								<img src="images/level.gif" width="${7*articleC.TPC_level }"
									height="16">
								</c:if>
								<!-- 게시물번호 -->							
								<a href="/TDL/TPC_COMMENT_content.do?TPC_num=${articleC.TPC_num}&pageNumC=${pgListC.currentPage}"></a>																
								
						<!-- 수정해야함 -->			
						<!-- 출력데이터 -->
						
						<td align="left" >[${articleC.TPC_id }]<br>	
													<h5>${articleC.TPC_content }</h5><br>	
													${articleC.TPC_date.substring(0,4)}년 ${articleC.TPC_date.substring(4,6)}월 ${articleC.TPC_date.substring(6,8) }일 작성 </td>				
						
						<td align="right" ><button class="btn btn-primary">${articleC.TPC_good }</button><button class="btn btn-danger" >${articleC.TPC_bad }</button>
						
					</tr>
				</c:forEach>
			</table>
		</c:if>

<!--  드롭다운으로 변경 예정(케럿) -->
			<c:if test="${pgListC.startPage > pgListC.blockSize}">
				<a href="/TDL/TDL_COMMENT_list.do?pageNumC=${pgListC.startPage-pgListC.blockSize}">[이전]</a>
			</c:if>
			
			<c:forEach var="i" begin="${pgListC.startPage}" end="${pgListC.endPage}">
				<a href="/TDL/TDL_COMMENT_list.do?pageNumC=${i}">
				<c:if test="${pgListC.currentPage==i}">
					<font color="red"><b>[${i}]</b></font>
				</c:if>
				<c:if test="${pgListC.currentPage!=i}">
					[${i}]
				</c:if>
				</a>
			</c:forEach>
			<c:if test="${pgListC.endPage < pgListC.pageCount }">
				<a href="/TDL/TDL_COMMENT_list.do?pageNumC=${pgListC.startPage+pgListC.blockSize}">[다음]</a>
			</c:if>
			</div>

<p>




</body>
</html>