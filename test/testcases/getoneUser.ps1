$expectedOutput = @{
    "trial" = "bob";
}

# Set the login credentials
$username = "bob"
$password = "temp1234!"

# Create headers with Content-Type
$headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
$headers.Add("Content-Type", "application/json")

# Prepare the login request body
$loginBody = @{
    username = $username
    password = $password
} | ConvertTo-Json

# Perform the login request and store the session in a session variable
$response = Invoke-RestMethod 'http://localhost:8080/login' -Method 'POST' -Headers $headers -Body $loginBody -SessionVariable session

# Perform authenticated GET request using the session
$response = Invoke-RestMethod 'http://localhost:8080/users/bob' -Method 'GET' -Headers $headers -WebSession $session

if($response.username -eq $expectedOutput["trial"]) {
        Write-Host "trial PASSED" -ForegroundColor Green
    }
else {
 $example =  $expectedOutput["trial"]
$result = $response.username
        Write-Host "trial FAILED. Expected: $example but got $result" -ForegroundColor Red
    }

Write-Host "Hello"

Write-Host $response

# Output the response
$response | ConvertTo-Json