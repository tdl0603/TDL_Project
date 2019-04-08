<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="top.jsp"/>


<!-- 게시판 글 내용 ====================================================-->

<div class="container">
		<div class="row">
				<table class="table table-striped"
					style="text-align: center; border: 1px solid #dddddd">
					<thead>
						<tr>
							<th colspan="3"
								style="background-color: #eeeeee; text-align: center;">게시판
								글 보기 양식</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="width: 20%;">글 제목</td>
							<td colspan="2"> ${article.TP_title }</td>
						</tr>
						<tr>
							<td>작성자</td>
							<td colspan="2"> ${article.TP_id }</td>
						</tr>
						<tr>
							<td>작성일자</td>
							<td colspan="2">${article.TP_date }</td>
						</tr>
						<tr>
							<td>내용</td>
							<td colspan="2" style="height: 550px; text-align: left;">${article.TP_content }</td>
						</tr>
					</tbody>
				</table>
		
		</div>
	</div>

<center>
	<table class="table table-striped"
		style="text-align: center; border: 1px solid #dddddd">
		<tr>
			<input type="button" value="글수정"
				onclick="document.location.href='/TDL/TDL_POST_updateForm.do?TP_num=${article.TP_num}&pageNum=${pageNum}'">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="글삭제"
				onclick="document.location.href='/TDL/TDL_POST_deleteForm.do?TP_num=${article.TP_num}&pageNum=${pageNum}'">
			&nbsp;&nbsp;&nbsp;&nbsp;

			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="글목록"
				onclick="document.location.href='/TDL/TDL_POST_list.do?pageNum=${pageNum}'">
			</td>
		</tr>
	</table>
</center>



<!-- 여기부터는 댓글 =============================================================== -->
	
<div class="container">
	<div class="row">
	<!-- 댓글 데이터 전송 -->
	<form method="post" name="TPC_writeform" action="/TDL/TDL_COMMENT_writePro.do"
		onsubmit="return writeSave()">
		<!-- TPC_num에는 자유게시물 번호가 저장된다. -> TP_num -->
		
			<input type="hidden" name="TPC_num" value="${article.TP_num }">
		<table class="table table-striped"
			style="text-align: center; border: 1px solid #dddddd">
			<thead>

				<tr>
					<td ><textarea class="form-control" colspan="3" placeholder="글 내용"
							name="TPC_content" maxlength="2048" style="height: 80px;"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan=2 align="center">
						<input type="submit" class="btn btn-primary" value="글쓰기" style=width:100%;height:100%>
					</td>
				</tr>	
			</tbody>

		</table>
		</form>
		<!-- 댓글 -->			
	<!-- 데이터의 댓글 유무 -->
		<div class="container" >
	
		<c:if test="${pgListC.count > 0 }">
			
			<!-- 데이터 보내기 댓글 -->
			댓글이 없쪙
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
				<a href="/TDL/TDL_POST_content.do?TP_num=${article.TP_num}&pageNum=${pgList.currentPage}&pageNumC=${pgListC.startPage-pgListC.blockSize}">[이전]</a>
			</c:if>
			
			<c:forEach var="i" begin="${pgListC.startPage}" end="${pgListC.endPage}">
				<a href="/TDL/TDL_POST_content.do?TP_num=${article.TP_num}&pageNum=${pgList.currentPage}&pageNumC=${i}">
				<c:if test="${pgListC.currentPage==i}">
					<font color="red"><b>[${i}]</b></font>
				</c:if>
				<c:if test="${pgListC.currentPage!=i}">
					[${i}]
				</c:if>
				</a>
			</c:forEach>
			<c:if test="${pgListC.endPage < pgListC.pageCount }">
				<a href="/TDL/TDL_POST_content.do?TP_num=${article.TP_num}&pageNum=${pgList.currentPage}&pageNumC=${pgListC.startPage+pgListC.blockSize}">[다음]</a>
			</c:if>
			</div>

	</div>
</div>

<jsp:include page="bottom.jsp"/>