<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="nav">
	<!-- nav1 -->
	<div class="sb-sidenav-menu-heading">라이브러리</div>
	<a class="nav-link" href="/api">
		<div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
			APIs
	</a>
	<!-- nav2 -->
	<div class="sb-sidenav-menu-heading">앱 정보</div>
	<a class="nav-link" href="/client">
		<div class="sb-nav-link-icon"><i class="fas fa-columns"></i></div>
		내 애플리케이션
	</a>
	<!-- nav3 -->
	<a class="nav-link collapsed" href="/client" data-toggle="collapse" data-target="#collapseLayouts" aria-expanded="false" aria-controls="collapseLayouts">
		<div class="sb-nav-link-icon"><i class="fas fa-columns"></i></div>
		nav3
		<div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
	</a>
	<div class="collapse" id="collapseLayouts" aria-labelledby="headingOne" data-parent="#sidenavAccordion">
		<nav class="sb-sidenav-menu-nested nav">
			<a class="nav-link" href="layout-static.html">nav3-sub1</a>
			<a class="nav-link" href="layout-sidenav-light.html">nav3-sub2</a>
		</nav>
	</div>
    
</div>
                
