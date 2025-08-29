# PowerShell script to set up GitHub repository and upload JAR file
# Replace YOUR_USERNAME and YOUR_REPO_NAME with your actual GitHub details

Write-Host "Setting up GitHub repository for Webhook SQL Solver..." -ForegroundColor Green

# Check if git is installed
if (!(Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Host "Git is not installed. Please install Git first." -ForegroundColor Red
    exit 1
}

# Initialize git repository
if (!(Test-Path ".git")) {
    Write-Host "Initializing git repository..." -ForegroundColor Yellow
    git init
}

# Add all files
Write-Host "Adding files to git..." -ForegroundColor Yellow
git add .

# Commit changes
Write-Host "Committing changes..." -ForegroundColor Yellow
git commit -m "Initial commit: Webhook SQL Solver Spring Boot application"

# Copy JAR file to root for easier access
Write-Host "Copying JAR file to repository root..." -ForegroundColor Yellow
Copy-Item "target\webhook-sql-solver-1.0.0.jar" "webhook-sql-solver-1.0.0.jar"

# Add JAR file
git add webhook-sql-solver-1.0.0.jar
git commit -m "Add compiled JAR file"

Write-Host "`nNext steps:" -ForegroundColor Green
Write-Host "1. Create a new repository on GitHub.com" -ForegroundColor White
Write-Host "2. Run the following commands:" -ForegroundColor White
Write-Host "   git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git" -ForegroundColor Cyan
Write-Host "   git branch -M main" -ForegroundColor Cyan
Write-Host "   git push -u origin main" -ForegroundColor Cyan
Write-Host "`n3. Your raw download link will be:" -ForegroundColor White
Write-Host "   https://github.com/YOUR_USERNAME/YOUR_REPO_NAME/raw/main/webhook-sql-solver-1.0.0.jar" -ForegroundColor Cyan

Write-Host "`nRepository setup complete!" -ForegroundColor Green
