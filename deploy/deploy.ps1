param(
    [string]$CI_COMMIT_REF_NAME
)

# Load the Docker image from the tar file
Write-Host "C:\Users\l1ds\bytebrigade\$CI_COMMIT_REF_NAME\bin\$CI_COMMIT_REF_NAME.tar"
docker load -i "C:\Users\l1ds\bytebrigade\$CI_COMMIT_REF_NAME\bin\$CI_COMMIT_REF_NAME.tar"

# Run the Docker container !
Write-Host "C:\Users\l1ds\bytebrigade\$CI_COMMIT_REF_NAME\config\application.properties"
docker run --name $CI_COMMIT_REF_NAME --env-file "C:\Users\l1ds\bytebrigade\$CI_COMMIT_REF_NAME\config\application.properties" -p 8080:8080 -d $CI_COMMIT_REF_NAME