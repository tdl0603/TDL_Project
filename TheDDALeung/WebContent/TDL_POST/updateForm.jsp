<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="top.jsp"/>
	<center>
		<!-- "onsubmit="return 호출할함수명()"> -->
		<form method="post" name="writeform"
				action="/TDL/TDL_POST_updatePro.do?pageNum=${pageNum}"
				onsubmit="return writeSave()">			
			<input type="hidden" name="TP_num" value="${article.TP_num }">
			<div class="container">
				<div class="row">
					<table class="table table-striped"
					style="text-align: center; border: 1px solid #dddddd">
					<thead>
						<tr>
							<th colspan="2"
								style="background-color: #eeeeee; text-align: center;">자유게시판
								</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input type="text" class="form-control"
								placeholder="글 제목" name="TP_title" maxlength="50" value="${article.TP_title }"></td>
						</tr>
						<tr>
							<td><textarea class="form-control"
									placeholder="글 내용" name="TP_content" maxlength="2048" value="${article.TP_content }"
									style="height: 350px;"></textarea></td>
						</tr>
					</tbody>
				</table>
<table>
						<tr>
							<input type="submit" class="btn btn-primary pull-right" value="글수정">
							<input type="reset" class="btn btn-primary pull-right" value="다시작성"> 
							<input type="button" class="btn btn-primary pull-right" value="목록보기"
								OnClick="document.location.href='/TDL/TDL_POST_list.do'"></td>
						</tr>
				</table>		
					
				</div>
			</div>

		</form>
		<jsp:include page="bottom.jsp"/>
