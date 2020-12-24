<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/header.jsp" %>
<main>
    <div class="container-fluid">
        <h1 class="mt-4">내 어플리케이션</h1>
        <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item active">전체</li>
        </ol>
    </div>
    ${clients}
</main>
<%@ include file="/WEB-INF/view/include/footer.jsp" %>
