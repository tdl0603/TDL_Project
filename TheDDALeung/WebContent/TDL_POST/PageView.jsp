<%@ page contentType="text/html;charset=EUC-KR"%>
<%
    int nowPage = 0;
    int nowBlock = 0;
    int totalRecord = Integer.parseInt(request.getParameter("totalRecord"));
    int numPerPage = 10;           
    int pagePerBlock = 10;    
    
	int totalPage =(int)Math.ceil((double)totalRecord / numPerPage);
	int totalBlock =(int)Math.ceil((double)totalPage / pagePerBlock);
	 
	if (request.getParameter("nowPage") != null){ 
		 nowPage= Integer.parseInt(request.getParameter("nowPage")); } 
	if (request.getParameter("nowBlock") != null){
		 nowBlock = Integer.parseInt(request.getParameter("nowBlock"));}
	int beginPerPage =   nowPage * numPerPage;	 
%>
<html>
<head><title>JSPBoard</title>
<meta http-equiv="Content-Type" content="text/html; charset="UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1">
<link rel="stylesheet" href="css/bootstrap.css">
<script>
function preBlock(nowBlock,nowPage){
	     document.total.nowBlock.value = nowBlock;
		 document.total.nowPage.value = nowPage;
	     total.submit();
	}

function prePage(nowBlock,nowPage){
	     document.total.nowBlock.value = nowBlock;
		 document.total.nowPage.value = nowPage;
	     total.submit();
	}

function afterBlock(nowBlock,nowPage){
	     document.total.nowBlock.value = nowBlock;
		 document.total.nowPage.value = nowPage;
	     total.submit();
	}
</script>
</head>
<body><center><br>
 <h2>����¡ & ��� ó�� �׽�Ʈ</h2>
 <br>
  <table>
   <tr>
   <td>�Խù���ȣ : &nbsp;</td>
	<%
	for (int i = beginPerPage;i < (beginPerPage+numPerPage); i++) { 
		if (i==totalRecord){ break;}
	%>
	<td align=center><%= totalRecord - i %>&nbsp;</td>
	<%}%>
 </tr>
 </table><p>
 <table>
  <tr>
   <td align="left" > 
	<% if(totalRecord !=0){ %> Go to Page 
	<% if (nowBlock > 0) {%> 
	<a href="javascript:preBlock('<%=nowBlock - 1 %>','<%=((nowBlock - 1) * pagePerBlock)%>')">
	���� <%=pagePerBlock %>��</a>
	<%}%> 

	:::

	<%
	   for (int i = 0; i < pagePerBlock; i++) { %>
	   <a href="javascript:prePage('<%=nowBlock%>','<%=(nowBlock*pagePerBlock) + i %>')">
	<%=(nowBlock * pagePerBlock) + i + 1 %></a>
    <% if ((nowBlock * pagePerBlock) + i + 1 == totalPage) { break; } %>
	<%} %>

	::: 

	<% if (totalBlock > nowBlock + 1) { %> 
	<a href="javascript:afterBlock('<%=nowBlock + 1 %>','<%=((nowBlock +1) * pagePerBlock)%>')">
	���� <%=pagePerBlock%>��</a>
	<%}%>

	<%} else{out.println("��ϵ� �Խù��� �����ϴ�."); }%>
    </td>
   </tr>
  </table>
  <form name="total">
   <input type="hidden" name ="totalRecord" value="<%=totalRecord%>">
   <input type="hidden" name ="nowBlock">
   <input type="hidden" name ="nowPage">
  </form>
  </center>
  	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
 </body>
</html>