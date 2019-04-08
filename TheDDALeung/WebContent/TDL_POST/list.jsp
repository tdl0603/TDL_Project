<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	//int currentPage=(Integer)request.getAttribute("currentPage");//Object-> Integer ->int
	//Hashtable pgList=(Hashtable)request.getAttribute("pgList");
	//pgList.get("count") => ${pgList.count}
	//${currentPage}
%>

<jsp:include page="top.jsp"/>
 <center>

<h1>자유 게시판</h1>
		<!-- 데이터의 유무 -->
		<c:if test="${pgList.count==0}">
			<table border="1" width="700" cellpadding="0" cellspacing="0"
				align="center">
				<tr>
					<td align="center">게시판에 저장된 글이 없습니다.</td>
				</tr>
			</table>
		</c:if>
		<c:if test="${pgList.count != 0 }">
<!--  여기부터 게시판 목록리스트 ======================================================================================== -->
	<div class="container">
		<div class="row">
	
			<table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th style="background-color: #eeeeee; text-align: center;">글번호</th>
						<th style="background-color: #eeeeee; text-align: center;">글제목</th>
						<th style="background-color: #eeeeee; text-align: center;">작성자</th>
						<th style="background-color: #eeeeee; text-align: center;">작성일</th>
					</tr>
				</thead>
				<!-- 실질적으로 레코드를 출력시켜주는 부분  
						이벤트를 발생시킨 객체를 구분하는 키워드 -> this
				-->
				<c:set var="number" value="${pgList.number}" />
				<c:forEach var="article" items="${articleList}">
					<tr onmouseover="this.style.backgroundColor='#e0ffff'"
						onmouseout="this.style.backgroundColor='white'">
						<td><c:out value="${number}" /> 
						<c:set var="number" value="${number-1}" /></td>
						<td>
							 <!-- 게시물번호 --> 
							 <a href="/TDL/TDL_POST_content.do?TP_num=${article.TP_num}&pageNum=${pgList.currentPage}">${article.TP_title}<td></a>
							${article.TP_id }
						</td>
					
						<td>${article.TP_date.substring(0,10)}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
		</c:if>
	<!-- 자유게시판 컬럼 -->
	
	

		<c:if test="${pgList.startPage > pgList.blockSize}">
			<a href="/TDL/TDL_POST_list.do?pageNum=${pgList.startPage-pgList.blockSize}&search=${search}&searchtext=${searchtext}">
				<input type="button" class="btn btn-success" value="이전"></a>
		</c:if>

		<c:forEach var="i" begin="${pgList.startPage}" end="${pgList.endPage}">
			<a href="/TDL/TDL_POST_list.do?pageNum=${i}&search=${search}&searchtext=${searchtext}">
				<c:if test="${pgList.currentPage==i}">
					<button class="btn btn-primary">${i}</button>
				</c:if> <c:if test="${pgList.currentPage!=i}">
					<button class="btn btn-info">${i}</button>
				</c:if>
			</a>
		</c:forEach>

		<c:if test="${pgList.endPage < pgList.pageCount }">
			<a href="/TDL/TDL_POST_list.do?pageNum=${pgList.startPage+pgList.blockSize}&search=${search}&searchtext=${searchtext}">
				<input type="button" class="btn btn-success" value="다음"></a>
		</c:if>
			<a href="/TDL/TDL_POST_writeForm.do" class="btn btn-link">글쓰기</a	>
			<%-- ?TP_num=${article.TP_num} --%>
		<p>
			<!-- 검색어 추가(자주 검색이 되는 항목을 잘 선택) 제목, 작성자, 제목+본문 -->
		<form name="test" action="/TDL/TDL_POST_list.do">
			<select name="search">
				<option value="TP_title">제목</option>
				<option value="subject_content">제목+본문</option>
				<option value="subject_writer">작성자</option>
			</select> <input type="text" size="15" name="searchtext">&nbsp; <input
				type="submit" value="검색">
		
		</form>
<!--  여기까지 게시판 목록리스트 ======================================================================================== -->


	</center> 
	<jsp:include page="bottom.jsp"/>
