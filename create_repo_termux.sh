#!/data/data/com.termux/files/usr/bin/bash
# ==============================================================================
# Mikasa Gallery — AMOLED Pitch Black & Onyx Yellow (YTX Liquid Glass Edition)
# Fresh GitHub Repository Creator & Initial Push Script for Termux / Linux
# Author: Mayank (@xboyrex)
# ==============================================================================
set -euo pipefail

# Default Configuration (can be overridden via environment variables or prompts)
GITHUB_USER="${GITHUB_USER:-xboyrex}"
GITHUB_EMAIL="${GITHUB_EMAIL:-xboyrex496@gmail.com}"
REPO_NAME="${REPO_NAME:-Mikasa-Gallery}"
REPO_DESC="AMOLED Pure Pitch Black & Onyx Yellow Android Gallery with YTX 3-Layer Refractive Liquid Glass Navigation Bar"
VISIBILITY="${VISIBILITY:-public}" # public or private

echo "=================================================================="
echo "  Mikasa Gallery — Fresh GitHub Repo & Termux Push Setup"
echo "  Theme   : 100% Pure AMOLED Pitch Black (#000000) & Onyx Yellow (#FFD600)"
echo "  Nav Bar : YTX 3-Layer Refractive Liquid Glass (Floating)"
echo "  Author  : Mayank (@${GITHUB_USER})"
echo "=================================================================="

# 1. Ensure required packages are installed if running inside Termux
if command -v pkg >/dev/null 2>&1; then
    echo "[1/6] Checking Termux dependencies (git, curl, gh, jq)..."
    for tool in git curl jq; do
        if ! command -v "$tool" >/dev/null 2>&1; then
            pkg install -y "$tool"
        fi
    done
fi

cd "$(dirname "$0")"

# 2. Ask for GitHub Personal Access Token if gh CLI is not authenticated and GITHUB_TOKEN is unset
if [ -z "${GITHUB_TOKEN:-}" ]; then
    if ! (command -v gh >/dev/null 2>&1 && gh auth status >/dev/null 2>&1); then
        echo ""
        read -r -p "Enter your GitHub Personal Access Token (repo scope): " GITHUB_TOKEN
    fi
fi

read -r -p "Repository name [${REPO_NAME}]: " INPUT_REPO
if [ -n "${INPUT_REPO}" ]; then
    REPO_NAME="${INPUT_REPO}"
fi

# 3. Reset any previous git history to start 100% fresh
echo "[2/6] Initializing fresh Git repository..."
rm -rf .git
git init -b main
git config user.name "${GITHUB_USER}"
git config user.email "${GITHUB_EMAIL}"

# Fix Termux safe.directory if project is on shared storage (/sdcard)
git config --global --add safe.directory "$(pwd)" || true

chmod +x gradlew deploy.sh capture_screenshot.sh deploy_nonandroid.sh 2>/dev/null || true

# 4. Stage & create initial commit
echo "[3/6] Staging all project files and creating fresh initial commit..."
git add -A
git commit -m "feat: initial release of Mikasa Gallery (AMOLED Pitch Black & Onyx Yellow + YTX Liquid Glass)"

# 5. Create brand-new GitHub repository via gh CLI or GitHub REST API
echo "[4/6] Creating GitHub repository ${GITHUB_USER}/${REPO_NAME}..."
if command -v gh >/dev/null 2>&1 && gh auth status >/dev/null 2>&1; then
    gh repo create "${GITHUB_USER}/${REPO_NAME}" --"${VISIBILITY}" --description "${REPO_DESC}" --source=. --remote=origin || true
elif [ -n "${GITHUB_TOKEN:-}" ]; then
    PRIVATE_FLAG="false"
    if [ "${VISIBILITY}" = "private" ]; then
        PRIVATE_FLAG="true"
    fi
    HTTP_CODE=$(curl -s -o /tmp/gh_repo_resp.json -w "%{http_code}"         -X POST         -H "Authorization: token ${GITHUB_TOKEN}"         -H "Accept: application/vnd.github+json"         https://api.github.com/user/repos         -d "{"name":"${REPO_NAME}","description":"${REPO_DESC}","private":${PRIVATE_FLAG},"has_issues":true,"has_projects":true,"has_wiki":false}")
    if [ "$HTTP_CODE" = "201" ]; then
        echo ">>> Successfully created repository: https://github.com/${GITHUB_USER}/${REPO_NAME}"
    elif [ "$HTTP_CODE" = "422" ]; then
        echo ">>> Repository ${GITHUB_USER}/${REPO_NAME} already exists on GitHub — linking remote."
    else
        echo ">>> Warning: GitHub API returned HTTP $HTTP_CODE"
        cat /tmp/gh_repo_resp.json || true
    fi
    git remote remove origin 2>/dev/null || true
    git remote add origin "https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/${GITHUB_USER}/${REPO_NAME}.git"
else
    git remote remove origin 2>/dev/null || true
    git remote add origin "https://github.com/${GITHUB_USER}/${REPO_NAME}.git"
fi

# 6. Push main branch & v1.0.0 release tag to trigger GitHub Actions APK build
echo "[5/6] Pushing main branch to GitHub..."
git push -u origin main --force

echo "[6/6] Tagging v1.0.0 and pushing tag to trigger automated Release APK..."
git tag -f v1.0.0
git push origin v1.0.0 --force

# Clean token from remote URL after push for security
git remote set-url origin "https://github.com/${GITHUB_USER}/${REPO_NAME}.git"

echo ""
echo "=================================================================="
echo "  DONE! Fresh repository is live at:"
echo "  https://github.com/${GITHUB_USER}/${REPO_NAME}"
echo "  GitHub Actions APK Build & Release:"
echo "  https://github.com/${GITHUB_USER}/${REPO_NAME}/actions"
echo "=================================================================="
