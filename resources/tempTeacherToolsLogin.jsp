<!DOCTYPE html>
<html>
<head>
    <title>Bootstrap Examples</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MathSpring Login</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container panel panel-default" style="width:50%;">
    <div class="panel-body">
        <h2>Teacher Tools Temporary Login</h2>
        <form action="${pageContext.request.contextPath}/WoAdmin?action=AdminTeacherLogin" method="post">
            <div class="form-group">
                <label for="userName">Email:</label>
                <input type="text" id="userName" class="form-control"  placeholder="Username" name="userName">
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" class="form-control" placeholder="Enter password" name="password">
            </div>
            <div class="checkbox">
                <label><input type="checkbox" name="remember"> Remember me</label>
            </div>
            <button type="submit" name="login" value="Login" class="btn btn-default">Submit</button>
        </form>
    </div>
</div>

</body>
</html>
