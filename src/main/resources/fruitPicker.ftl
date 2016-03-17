<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Fruit Picker</title>
</head>
<body>
<form action="/favorite_fruit" method="post">
    <p>What is your favorite fruit?</p>
    <#list fruits as fruit>
        <p>
            <input type="radio" name="fruit" value="${fruit}">${fruit}</input>
        </p>
    </#list>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>