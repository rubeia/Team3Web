
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Outstanding Requests</title>
</head>
<body>
    <h1>Outstanding Requests</h1>
    <table>
        <tr>
            <th>Name</th>
            <th>Requester</th>
            <th>Date</th>
            <th>Comments</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="app" items="${applications}">
            <tr>
                <td>${app.name}</td>
                <td>${app.requester}</td>
                <td>${app.requestDate}</td>
                <td>
                    <form action="NextGenRentServlet" method="post">
                        <input type="hidden" name="appId" value="${app.id}">
                        <textarea name="comment"></textarea>
                        <input type="submit" name="action" value="Approve">
                        <input type="submit" name="action" value="Deny">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
