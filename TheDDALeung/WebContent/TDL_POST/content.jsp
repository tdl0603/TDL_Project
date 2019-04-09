<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="top.jsp"/>
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

<form method="post" name="writeform" action="/TDL/TDL_COMMENT/writePro.do"
			onsubmit="return writeSave()">

<div class="container">
	<div class="row">
		<table class="table table-striped"
			style="text-align: center; border: 1px solid #dddddd">
			<%-- <input type="hidden" value="${article.TP_num }" /> --%>
			<thead>
			<tr>
					<td><textarea class="form-control" colspan="3" placeholder="글 내용"
							name="TPC_content" maxlength="2048" style="height: 80px;"></textarea></td>
				</tr>
				<input type="hidden" name="TP_num"  value="${article.TP_num}">
				<tr>
				<td colspan=2 align="center">
				<input type="submit" name="TPC_comment"class="btn btn-primary" value="글쓰기" style=width:100%;height:100%>
								</td>
								</tr>	 	
			</thead>
		</table>
	</div>
</div>
</form>

<!-- 댓글 -->
		<!-- 데이터의 댓글 유무 -->
		<div class="container">
			<c:if test="${pgListC.count > 0 }">
				<!-- 데이터 보내기 댓글 -->
			<!-- 댓글 출력 테이블  -->
				<table class="table">
					<!-- 실질적으로 레코드를 출력시켜주는 부분  
						이벤트를 발생시킨 객체를 구분하는 키워드 -> this
				-->
				
		<!--   String addr =Integer.toString(TPC_num)+"c"+countC+"c"+0; // 댓글일련번호를 댓글출력시 부여해서 삭제시 그 번호로 삭제하기	 -->
					<c:set var="countC" value="${countC}"/>
					<c:set var="number" value="${pgListC.number}" />
					<c:forEach var="articleC" items="${articleListC}">
						<tr>
							<!-- 답변글인 경우 먼저 답변이미지를 부착시키는 코드부분 -->
							<c:if test="${articleC.TPC_level > 1 }">
								<h5>답글 :</h5> 
							</c:if>
							<!-- 수정해야함 --> 
								<!-- 출력데이터 -->
						
							<c:if test="${article.TP_num==articleC.TPC_num }">
								<td align="left">[${articleC.TPC_id }]<br>
									<h5>${articleC.TPC_content }</h5>
									<br> ${articleC.TPC_date.substring(0,4)}년
											${articleC.TPC_date.substring(5,7)}월
											${articleC.TPC_date.substring(8,10) }일 
											${articleC.TPC_date.substring(11,19) } 작성 													 
						<td><br>&nbsp;<br>	&nbsp;<br>&nbsp;<br> 				 		 	
							<a onclick="return confirm('좋아요 하시겠습니까?')" >좋아요</a> <span style="color: green;">(좋아요:0)</span>
						  	<a onclick="return confirm('싫어요 하시겠습니까?')" >싫어요</a><span style="color: red;">(싫어요: 0)</span>
						  		<form  method="post" action="/TDL/TC_delete.do">
						  			<input type="hidden" name="TPC_num" value="${articleC.TPC_num}">
						  			<input type="hidden" name="countC" value="${countC}">
						  			<input type="submit" value="글삭제"></a>
						  		</form>
						  	<c:set var="countC" value="${countC-1}" />
						  	
							</td>												  
											  				
										<!-- 답글달기 기능 -->			
										<!-- 	<button class="btn btn-success" id="comment2">답글달기</button> -->
								</td>	 
											 
							</c:if>
						</tr>
				<!-- ===================댓글의 댓글 구현중============================== -->
<%-- 				
					<form id="slideToggle" method="post" name="TPC_writeform"
								action="/TDL/TDL_COMMENT_writePro.do">
								<!-- TPC_num에는 자유게시물 번호가 저장된다. -> TP_num -->
	
							<table class="table table-striped"
										style="text-align: center; border: 1px solid #dddddd">
							<thead>	
								<input type="hidden" name="TP_num" value="${article.TP_num }"/>
								<input type="hidden" name="pageNum" value="${pgList.currentPage} }"/>
							<tr>
								<td><textarea class="form-control" colspan="3"
									placeholder="글 내용" name="TPC_content" maxlength="2048"
									style="height: 80px;"></textarea></td>
							</tr>
							<tr>
								<td colspan=2 align="center"><input type="submit"
									class="btn btn-primary" value="글쓰기"
									style="width: 100%; height: 100%">
								</td>
							</tr>
						</thead>

						</table>
					</form>
			 --%>
							</c:forEach>				
				</table>			
			</c:if>
			<!--  드롭다운으로 변경 예정(케럿) -->
	</div>
</div>



<jsp:include page="bottom.jsp"/>