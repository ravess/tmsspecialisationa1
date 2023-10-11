$expectedOutput = @{
    "loginwith.ps1" = "bob";
}

$testCasesPath = "$BIN_REPO_SVR_PATH\l1ds\$CI_COMMIT_REF_NAME\test\test_cases\"

Get-ChildItem -Path $testCasesPath -Filter *.ps1 | ForEach-Object {
    $scriptName = $_.Name
    Write-Host "Running $scriptName"

    $output = & "$testCasesPath\$scriptName"

    Write-Host $output
}

Write-Host "Tests completed!!"