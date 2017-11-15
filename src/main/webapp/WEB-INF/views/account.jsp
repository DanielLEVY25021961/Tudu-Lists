<%-- DOCTYPE (DTD pour HTML5) --%>
<!doctype html>

<%-- DIRECTIVE PAGE --%>
<%-- Définit le langage éventuellement incorporé dans la page (java) --%>
<%--Si la page est une page d'erreur, l'encodage de la page, ... --%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8" isErrorPage="false" 
	errorPage="/WEB-INF/views/error.jsp"%>
	
<%-- DECLARATION DE LA TagLib JSTL --%>		
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- DECLARATION DE LA TagLib JSTL --%>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- DECLARATION DE LA TagLib de SPRING --%>	
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<html xmlns="http://www.w3.org/1999/xhtml" lang="fr">

	<head>
	    <title>Tudu Lists</title>
	    <jsp:include page="../fragments/header_head.jsp"/>
	</head>
	
	<body id="main">
	
	<jsp:include page="../fragments/header_body.jsp"/>
	
		<c:if test="${updated eq 'ok'}">
		    <h1><fmt:message key="common.update.ok"/></h1>
		</c:if>
		
		<h3><fmt:message key="user.info.title"/></h3>
		
		<form:form commandName="user">
		
		    <table class="list" style="width:600px">
		        <tr>
		            <th colspan="3">
		                <fmt:message key="user.info.subtitle"/>
		            </th>
		        </tr>
		        <tbody>
		        <tr class="odd">
		            <td style="width: 200px;">
		                <fmt:message key="user.info.first.name"/>
		            </td>
		            <td>
		                <form:input path="firstName" size="15" maxlength="60"/>
		            </td>
		            <td>
		                <form:errors path="firstName" cssClass="error"/>
		            </td>
		        </tr>
		        <tr class="even">
		            <td>
		                <fmt:message key="user.info.last.name"/>
		            </td>
		            <td>
		                <form:input path="lastName" size="15" maxlength="60"/>
		            </td>
		            <td>
		                <form:errors path="lastName" cssClass="error"/>
		            </td>
		        </tr>
		        <tr class="odd">
		            <td>
		                <fmt:message key="user.info.email"/>
		            </td>
		            <td>
		                <form:input path="email" size="25" maxlength="100"/>
		            </td>
		            <td>
		                <form:errors path="email" cssClass="error"/>
		            </td>
		        </tr>
		        <tr class="even">
		            <td>
		                <fmt:message key="user.info.date.format"/>
		            </td>
		            <td>
		                <form:select path="dateFormat">
		                    <form:option value="MM/dd/yyyy" label="mm/dd/yyyy" />
		                    <form:option value="MM/dd/yy" label="mm/dd/yy" />
		                    <form:option value="dd/MM/yyyy" label="dd/mm/yyyy" />
		                    <form:option value="dd/MM/yy" label="dd/mm/yy" />
		                </form:select>
		            </td>
		            <td></td>
		        </tr>
		        <tr class="odd">
		            <td>
		                <fmt:message key="user.info.password"/>
		            </td>
		            <td>
		                <form:password path="password" size="15" maxlength="32"/>
		            </td>
		            <td>
		                <form:errors path="password" cssClass="error"/>
		            </td>
		        </tr>
		        <tr class="even">
		            <td>
		                <fmt:message key="user.info.verifypassword"/>
		            </td>
		            <td>
		                <form:password path="verifyPassword" size="15" maxlength="32"/>
		            </td>
		            <td>
		                <form:errors path="verifyPassword" cssClass="error"/>
		            </td>
		        </tr>
		        <tr>
		            <td colspan="3" style="padding-left: 250px;">
		                <br/>
		                <a class="button" href="javascript:submitForm();"
		                   onclick="this.blur();"><span><fmt:message key="form.submit"/></span></a>
		                <br/><br/>
		            </td>
		        </tr>
		        </tbody>
		    </table>
		</form:form>
		
		<jsp:include page="../fragments/footer.jsp"/>
	
	</body>
	
</html>