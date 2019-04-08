<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="top.jsp"/>

	<center>
		<!-- "onsubmit="return 호출할함수명()"> -->
		<form method="post" name="writeform" action="/TDL/TDL_POST_writePro.do"
			onsubmit="return writeSave()">
			
			<input type="hidden" name="TP_num" value="${TP_num }">


			<div class="container">
				<div class="row">
					<table class="table table-striped"
					style="text-align: center; border: 1px solid #dddddd">
					<thead>
						<tr>
							<th colspan="2"
								style="background-color: #eeeeee; text-align: center;">게시판
								글쓰기 양식</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><input type="text" class="form-control"
								placeholder="글 제목" name="TP_title" maxlength="50"></td>
						</tr>
						<tr>
							<td><textarea class="form-control"
									placeholder="글 내용" name="TP_content" maxlength="2048"
									style="height: 350px;"></textarea></td>
						</tr>
					</tbody>
				</table>
				<table>

						<tr>
							<td colspan=2 bgcolor="#b0e0e6" align="center"><input
								type="submit" value="글쓰기" rows="13"> <input type="reset"
								value="다시작성"> <input type="button" value="목록보기"
								OnClick="window.location='/TDL/TDL_POST_list.do'"></td>
						</tr>
						
					</table>
				</div>
			</div>

		</form>
		<img src="TDL_POST/bottom.png"/>
		<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
		<script>
		function writeSave(){
			
		
			if(document.writeform.subject.value==""){
			  alert("제목을 입력하십시요.");
			  document.writeform.subject.focus();
			  return false;
			}
			
			if(document.writeform.content.value==""){
			  alert("내용을 입력하십시요.");
			  document.writeform.content.focus();
			  return false;
			}
		        
		 }    
		</script>
<jsp:include page="bottom.jsp"/>
